package com.supersuman.macronium

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
import com.iammert.library.AnimatedTabLayout

class PresetsFragment : Fragment() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var tabLayout: AnimatedTabLayout
    private lateinit var searchBar: TextInputEditText
    private lateinit var teleprinter : Teleprinter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = PresetsRecyclerViewAdapter(requireContext(), data)
        initListeners()

    }

    private fun initViews() {
        teleprinter = Teleprinter(requireActivity() as AppCompatActivity, true)
        recyclerView = requireActivity().findViewById(R.id.fragmentPresetsRecyclerView)
        tabLayout = requireActivity().findViewById(R.id.mainActivityTabLayout)
        searchBar = requireActivity().findViewById(R.id.fragmentPresetsSearchBar)
    }

    private fun initListeners() {
        searchBar.addTextChangedListener {
            recyclerView.adapter = PresetsRecyclerViewAdapter(requireContext(), getSearchResults(it.toString()))
        }
        teleprinter.addOnKeyboardClosedListener {
            searchBar.clearFocus()
        }
    }
}