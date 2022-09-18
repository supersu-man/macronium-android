package com.supersuman.macronium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commit451.teleprinter.Teleprinter
import com.google.android.material.textfield.TextInputEditText
import com.supersuman.macronium.adapters.PresetsRecyclerViewAdapter
import com.supersuman.macronium.R
import com.supersuman.macronium.data
import com.supersuman.macronium.getSearchResults

class PresetsFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextInputEditText
    private lateinit var teleprinter: Teleprinter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = PresetsRecyclerViewAdapter(requireContext(), data)
        initListeners()

    }

    private fun initViews(view: View) {
        recyclerView = view.findViewById(R.id.fragmentPresetsRecyclerView)
        searchBar = view.findViewById(R.id.fragmentPresetsSearchBar)
        teleprinter = Teleprinter(requireActivity() as AppCompatActivity, true)
    }

    private fun initListeners() {
        searchBar.addTextChangedListener {
            recyclerView.adapter =
                PresetsRecyclerViewAdapter(requireContext(), getSearchResults(it.toString()))
        }
        teleprinter.addOnKeyboardClosedListener {
            searchBar.clearFocus()
        }
    }
}