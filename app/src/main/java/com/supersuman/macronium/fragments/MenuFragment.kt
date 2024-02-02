package com.supersuman.macronium.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.activities.CreatePresetActivity
import com.supersuman.macronium.activities.PresetsActivity
import com.supersuman.macronium.R

class MenuFragment : Fragment() {

    private lateinit var presetsCard: MaterialCardView
    private lateinit var myPresetsCard: MaterialCardView
    private lateinit var createPresetCard: MaterialCardView
    private lateinit var permissionText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initListeners()

    }

    private fun initViews(view: View) {
        presetsCard = view.findViewById(R.id.presetsCard)
        myPresetsCard = view.findViewById(R.id.myPresetsCard)
        createPresetCard = view.findViewById(R.id.createPresetCard)
        permissionText = view.findViewById(R.id.permissionText)
    }

    private fun initListeners() {
        presetsCard.setOnClickListener {
            val intent = Intent(context, PresetsActivity::class.java)
            context?.startActivity(intent)
        }
        myPresetsCard.setOnClickListener {
            val intent = Intent(context, PresetsActivity::class.java)
            intent.putExtra("isCustom", true)
            context?.startActivity(intent)
        }
        createPresetCard.setOnClickListener {
            val intent = Intent(context, CreatePresetActivity::class.java)
            context?.startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        if (activity != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionText.visibility = View.VISIBLE
            } else {
                permissionText.visibility = View.GONE
            }
        }
    }


}