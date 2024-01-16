package com.supersuman.macronium.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.activities.CreatePresetActivity
import com.supersuman.macronium.activities.PresetsActivity
import com.supersuman.macronium.R

class MenuFragment : Fragment() {

    private lateinit var presetsCard: MaterialCardView
    private lateinit var myPresetsCard: MaterialCardView
    private lateinit var createPresetCard: MaterialCardView

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


}