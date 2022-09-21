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
import com.supersuman.macronium.appDatabase
import com.supersuman.macronium.other.*
import kotlin.concurrent.thread

class PresetsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var searchBar: TextInputEditText
    private lateinit var teleprinter: Teleprinter
    private lateinit var databaseDao: DatabaseDao

    private var pinnedPresets: MutableList<Preset> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        databaseDao.getPinnedPresets().observe(requireActivity()){
            pinnedPresets.clear()
            pinnedPresets.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }


    }

    private fun initViews(view: View) {
        databaseDao = appDatabase.databaseDao()
        recyclerView = view.findViewById(R.id.fragmentPresetsRecyclerView)
        searchBar = view.findViewById(R.id.fragmentPresetsSearchBar)
        teleprinter = Teleprinter(requireActivity() as AppCompatActivity, true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = PresetsRecyclerViewAdapter(pinnedPresets, databaseDao)
    }

}