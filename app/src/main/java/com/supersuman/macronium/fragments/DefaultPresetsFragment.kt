package com.supersuman.macronium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commit451.teleprinter.Teleprinter
import com.google.android.material.textview.MaterialTextView
import com.supersuman.macronium.R
import com.supersuman.macronium.appDatabase
import com.supersuman.macronium.other.DatabaseDao
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.data
import kotlin.concurrent.thread

class DefaultPresetsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var teleprinter: Teleprinter
    private lateinit var databaseDao: DatabaseDao

    private var pinnedPresets: MutableList<Preset> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_presets, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        initDatabase()
    }

    private fun initViews(view: View) {
        databaseDao = appDatabase.databaseDao()
        recyclerView = view.findViewById(R.id.fragmentPresetsRecyclerView)
        teleprinter = Teleprinter(requireActivity() as AppCompatActivity, true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = PresetsRecyclerViewAdapter(pinnedPresets, databaseDao)
    }

    private fun initDatabase() {
        databaseDao.getPinnedPresets().observe(requireActivity()) {
            pinnedPresets.clear()
            pinnedPresets.addAll(it)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

}


class PresetsRecyclerViewAdapter(private val pinnedPresets: MutableList<Preset>, private val presetDao: DatabaseDao) :
    RecyclerView.Adapter<PresetsRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: MaterialTextView = view.findViewById(R.id.presetsTextView1)
        val pin: ImageView = view.findViewById(R.id.presetImageView)
        val deleteButton: ImageView = view.findViewById(R.id.presetDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preset, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.deleteButton.visibility = View.GONE
        holder.textView.text = data[position].presetName
        if (isPinned(data[position])) {
            holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
            data[position].pinned = true
        }
        holder.pin.setOnClickListener {
            if (data[position].pinned) {
                holder.pin.setImageResource(R.drawable.ic_outline_push_pin_24)
                data[position].pinned = false
                thread { presetDao.deletePreset(data[position]) }
            } else {
                holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
                data[position].pinned = true
                thread { presetDao.insertPresets(data[position]) }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun isPinned(myPreset: Preset): Boolean {
        for (pinnedPreset in pinnedPresets) {
            if (pinnedPreset.presetName == myPreset.presetName) {
                return true
            }
        }
        return false
    }
}