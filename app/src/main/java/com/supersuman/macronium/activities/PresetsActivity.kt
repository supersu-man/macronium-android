package com.supersuman.macronium.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.google.android.material.textview.MaterialTextView
import com.supersuman.macronium.R
import com.supersuman.macronium.other.AppDatabase
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.PresetsDao
import com.supersuman.macronium.other.databaseName
import kotlin.concurrent.thread

class PresetsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_presets)

        val recyclerView: RecyclerView = findViewById(R.id.presetsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, databaseName).build()
        val presetDao = db.presetDao()

        if (intent.getBooleanExtra("isCustom", false)) {
            findViewById<TextView>(R.id.presetsTitle).text = "Custom Presets"
            thread {
                val customPresets: List<Preset> = presetDao.getCustomPresets()
                runOnUiThread {
                    recyclerView.adapter = PresetsAdapter(customPresets, presetDao)
                }
            }
        } else {
            findViewById<TextView>(R.id.presetsTitle).text = "Default Presets"
            thread {
                val defaultPresets: List<Preset> = presetDao.getDefaultPresets()
                runOnUiThread {
                    recyclerView.adapter = PresetsAdapter(defaultPresets, presetDao)
                }
            }
        }
    }
}

class PresetsAdapter(private val presets: List<Preset>, private val presetsDao: PresetsDao) :
    RecyclerView.Adapter<PresetsAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: MaterialTextView = view.findViewById(R.id.presetTitle)
        val pinButton: ImageView = view.findViewById(R.id.presetPinButton)
        val deleteButton: ImageView = view.findViewById(R.id.presetDeleteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.preset, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.title.text = presets[position].title
        if (presets[position].isCustom) {
            holder.deleteButton.visibility = View.VISIBLE
            holder.deleteButton.setOnClickListener {
                thread { presetsDao.deletePreset(presets[position]) }
            }
        }

        if (presets[position].isPinned) {
            holder.pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
        }

        holder.pinButton.setOnClickListener {
            if (presets[position].isPinned) {
                holder.pinButton.setImageResource(R.drawable.ic_outline_push_pin_24)
                presets[position].isPinned = false
                thread { presetsDao.updatePreset(presets[position]) }
            } else {
                holder.pinButton.setImageResource(R.drawable.ic_baseline_push_pin_24)
                presets[position].isPinned = true
                thread { presetsDao.updatePreset(presets[position]) }
            }
        }
    }

    override fun getItemCount(): Int {
        return presets.size
    }
}