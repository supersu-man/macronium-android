package com.supersuman.macronium

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray

class PresetsRecyclerViewAdapter(context: Context, private val mutableList : MutableList<MutableList<String>>) : RecyclerView.Adapter<PresetsRecyclerViewAdapter.ViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    private val pinnedList: MutableList<MutableList<String>> = loadOrderedCollection(sharedPreferences,"pinned")

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val textView1 : MaterialTextView = view.findViewById(R.id.presetsTextView1)
        val pin : ImageView = view.findViewById(R.id.presetImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preset, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView1.text = mutableList[position][0]
        if (mutableList[position] in pinnedList){
            holder.pin.tag = "pinned"
            holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
        }
        holder.pin.setOnClickListener {
            if (holder.pin.tag == "unpinned"){
                holder.pin.tag = "pinned"
                holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
                pinnedList.add(mutableList[position])
                saveOrderedCollection(sharedPreferences, pinnedList,"pinned")
            } else{
                holder.pin.tag = "unpinned"
                holder.pin.setImageResource(R.drawable.ic_outline_push_pin_24)
                pinnedList.remove(mutableList[position])
                saveOrderedCollection(sharedPreferences, pinnedList,"pinned")
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}