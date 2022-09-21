package com.supersuman.macronium.adapters

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.supersuman.macronium.R
import com.supersuman.macronium.other.*
import kotlin.concurrent.thread

class PresetsRecyclerViewAdapter(private val pinnedPresets: MutableList<Preset>, private val presetDao: DatabaseDao) : RecyclerView.Adapter<PresetsRecyclerViewAdapter.ViewHolder>() {


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
            thread {
                println(presetDao.getPinnedPresets())
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