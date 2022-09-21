package com.supersuman.macronium.fragments

import android.content.res.Resources
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.setMargins
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.supersuman.macronium.R
import com.supersuman.macronium.appDatabase
import com.supersuman.macronium.other.*
import kotlin.concurrent.thread


class CreatePresetFragment : Fragment() {

    private lateinit var chipGroup: ChipGroup
    private lateinit var selectedChipGroup: ChipGroup
    private lateinit var confirmChip: Chip
    private lateinit var databaseDao: DatabaseDao

    private val selectedChipsList: MutableList<String> = mutableListOf()
    private var myPresets: MutableList<Preset> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_preset, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        initListeners()

        addAllKeys()
    }

    private fun initViews(view: View) {
        databaseDao = appDatabase.databaseDao()
        chipGroup = view.findViewById(R.id.chipGroup)
        selectedChipGroup = view.findViewById(R.id.selectedChipGroup)
        confirmChip = view.findViewById(R.id.confirmChip)
    }

    private fun initListeners() {
        databaseDao.getMyPresets().observe(requireActivity()) {
            myPresets = it
        }
        confirmChip.setOnClickListener {
            if (selectedChipsList.isNotEmpty()) createDialogBox()
        }
    }

    private fun addAllKeys() {
        thread {
            try {
                for (key in Keys.values()) {
                    val chip = createAllKeysChip(key.name)
                    requireActivity().runOnUiThread { chipGroup.addView(chip) }
                }
            } catch (e: Exception) {
            }
        }
    }


    private fun createAllKeysChip(text: String): Chip {
        val chip = Chip(requireActivity())
        chip.text = text
        chip.isCheckable = true
        chip.isCheckedIconVisible = true
        val chip2 = Chip(requireActivity())
        chip2.text = text
        chip.setOnCheckedChangeListener { _, b ->
            if (b) {
                selectedChipGroup.addView(chip2)
                selectedChipsList.add(text)
            } else {
                selectedChipGroup.removeView(chip2)
                selectedChipsList.remove(text)
            }
        }
        return chip
    }

    private fun createDialogBox() {
        val materialDialogueBuilder = MaterialAlertDialogBuilder(requireActivity())
        val linearLayout = LinearLayout(requireActivity())
        val editText = EditText(requireActivity())
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        editText.layoutParams = params
        editText.hint = "Preset name..."
        linearLayout.addView(editText)
        linearLayout.setPadding(60, 60, 60, 0)
        materialDialogueBuilder.setView(linearLayout)
        materialDialogueBuilder.setPositiveButton("Create") { _, _ ->
            if (isNameOk(editText.text.toString())) {
                val obj = Preset()
                obj.presetName = editText.text.toString()
                obj.presetCommand = selectedChipsList
                obj.myPreset = true
                thread { databaseDao.insertPresets(obj) }
            } else {
                Toast.makeText(requireActivity(), "Name already exists...", Toast.LENGTH_SHORT).show()
            }
        }
        materialDialogueBuilder.setNegativeButton("Cancel") { _, _ -> }
        materialDialogueBuilder.show()
    }

    private fun isNameOk(name: String): Boolean {
        for (preset in myPresets) {
            if (preset.presetName == name) return false
        }
        return true
    }

}