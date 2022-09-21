package com.supersuman.macronium.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.R

class MenuFragment : Fragment() {

    private lateinit var presetsCard: MaterialCardView
    private lateinit var myPresetsCard: MaterialCardView
    private lateinit var createPresetCard: MaterialCardView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, PresetsFragment())
            transaction.addToBackStack(null).commit()
        }
        myPresetsCard.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, MyPresetsFragment())
            transaction.addToBackStack(null).commit()
        }
        createPresetCard.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.fragmentContainer, CreatePresetFragment())
            transaction.addToBackStack(null).commit()
        }
    }


}