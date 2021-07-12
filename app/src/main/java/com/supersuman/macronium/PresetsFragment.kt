package com.supersuman.macronium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textfield.TextInputEditText
import com.iammert.library.AnimatedTabLayout

class PresetsFragment : Fragment() {
    private lateinit var recyclerView : RecyclerView
    private lateinit var tabLayout: AnimatedTabLayout
    private lateinit var materialCardView: MaterialCardView
    private lateinit var searchBar: TextInputEditText
    private val presetsData = PresetsData()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        recyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        recyclerView.adapter = PresetsRecyclerViewAdapter(requireContext(),presetsData.data)
        tabLayout.setTabChangeListener(object : AnimatedTabLayout.OnChangeListener{
            override fun onChanged(position: Int) {
                when(position){
                    0 -> {
                        materialCardView.animate().translationY(0f)
                        searchBar.clearFocus()
                    }
                    1 -> materialCardView.animate().translationY(materialCardView.height.toFloat()+50)
                }
            }
        })
        searchBar.addTextChangedListener {
            recyclerView.adapter = PresetsRecyclerViewAdapter(requireContext(),presetsData.getSearchResults(it.toString()))
        }
    }



    private fun initViews() {
        recyclerView = requireActivity().findViewById(R.id.fragmentPresetsRecyclerView)
        materialCardView = requireActivity().findViewById(R.id.mainActivityButtonsParentCard)
        tabLayout = requireActivity().findViewById(R.id.mainActivityTabLayout)
        searchBar = requireActivity().findViewById(R.id.fragmentPresetsSearchBar)
    }
}