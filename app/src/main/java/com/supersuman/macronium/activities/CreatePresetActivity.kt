package com.supersuman.macronium.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.room.Room
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.supersuman.macronium.R
import com.supersuman.macronium.other.AppDatabase
import com.supersuman.macronium.other.Keys
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.PresetsDao
import com.supersuman.macronium.other.databaseName
import java.util.UUID
import kotlin.concurrent.thread

class CreatePresetActivity : AppCompatActivity() {
    private lateinit var chipGroup: ChipGroup
    private lateinit var selectedChipGroup: ChipGroup
    private lateinit var confirmChip: Chip
    lateinit var dao: PresetsDao

    private val selectedChipsList: MutableList<String> = mutableListOf()
    private var myPresets: MutableList<Preset> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_preset)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, databaseName).build()
        dao = db.presetDao()

        chipGroup = findViewById(R.id.chipGroup)
        selectedChipGroup = findViewById(R.id.selectedChipGroup)
        confirmChip = findViewById(R.id.confirmChip)

        thread {
            for (key in Keys.values()) {
                val chip = createAllKeysChip(key.name)
                runOnUiThread { chipGroup.addView(chip) }
            }
        }

        confirmChip.setOnClickListener {
            if (selectedChipsList.isNotEmpty()) createDialogBox()
        }

    }



    private fun createAllKeysChip(text: String): Chip {
        val chip = Chip(this)
        chip.text = text
        chip.isCheckable = true
        chip.isCheckedIconVisible = true
        val chip2 = Chip(this)
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
        val materialDialogueBuilder = MaterialAlertDialogBuilder(this)
        val linearLayout = LinearLayout(this)
        val editText = EditText(this)
        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        editText.layoutParams = params
        editText.hint = "Preset name..."
        linearLayout.addView(editText)
        linearLayout.setPadding(60, 60, 60, 0)
        materialDialogueBuilder.setView(linearLayout)
        materialDialogueBuilder.setPositiveButton("Create") { _, _ ->
            val newPreset = Preset(UUID.randomUUID(), editText.text.toString(), selectedChipsList, true, false)
            thread {
                try {
                    dao.insertPreset(newPreset)
                    runOnUiThread {
                        Toast.makeText(this, "Preset created", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        materialDialogueBuilder.setNegativeButton("Cancel") { _, _ -> }
        materialDialogueBuilder.show()
    }

}