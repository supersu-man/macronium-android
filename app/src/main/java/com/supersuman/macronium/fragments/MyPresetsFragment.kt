package com.supersuman.macronium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.supersuman.macronium.R
import com.supersuman.macronium.appDatabase
import com.supersuman.macronium.other.DatabaseDao
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.data
import kotlin.concurrent.thread

class MyPresetsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var databaseDao: DatabaseDao

    private var myPresets: MutableList<Preset> = mutableListOf()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_my_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
        databaseDao.getMyPresets().observe(requireActivity()) {
            myPresets.clear()
            myPresets.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun initViews(view: View) {
        databaseDao = appDatabase.databaseDao()
        recyclerView = view.findViewById(R.id.fragmentMyPresetsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = MyPresetsAdapter(myPresets, databaseDao)
    }

}

class MyPresetsAdapter(private val myPresets: MutableList<Preset>, private val databaseDao: DatabaseDao) :
    RecyclerView.Adapter<MyPresetsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val presetName: MaterialTextView = view.findViewById(R.id.presetsTextView1)
        val pin: ImageView = view.findViewById(R.id.presetImageView)
        val deleteButton: ImageView = view.findViewById(R.id.presetDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preset, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.presetName.text = myPresets[position].presetName
        holder.deleteButton.setOnClickListener {
            thread { databaseDao.deletePreset(myPresets[position]) }
        }
        if (myPresets[position].pinned) {
            holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
        }
        holder.pin.setOnClickListener {
            if (myPresets[position].pinned) {
                holder.pin.setImageResource(R.drawable.ic_outline_push_pin_24)
                myPresets[position].pinned = false
                thread { databaseDao.insertPresets(myPresets[position]) }
            } else {
                holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
                myPresets[position].pinned = true
                thread { databaseDao.insertPresets(myPresets[position]) }
            }
        }
    }

    override fun getItemCount(): Int {
        return myPresets.size
    }
}