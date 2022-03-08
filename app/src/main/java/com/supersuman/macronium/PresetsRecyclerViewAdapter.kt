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

class PresetsRecyclerViewAdapter(context: Context, private val mutableList : MutableList<Preset>) : RecyclerView.Adapter<PresetsRecyclerViewAdapter.ViewHolder>() {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    val s: MutableList<String> = loadOrderedCollection("presets")

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val textView1 : MaterialTextView = view.findViewById(R.id.presetsTextView1)
        val pin : ImageView = view.findViewById(R.id.presetImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.preset, parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.textView1.text = mutableList[position].displayName
        if (mutableList[position].key in s){
            holder.pin.tag = "pinned"
            holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
        }
        holder.pin.setOnClickListener {
            if (holder.pin.tag == "unpinned"){
                holder.pin.tag = "pinned"
                holder.pin.setImageResource(R.drawable.ic_baseline_push_pin_24)
                s.add(mutableList[position].key)
                saveOrderedCollection(s,"presets")
            } else{
                holder.pin.tag = "unpinned"
                holder.pin.setImageResource(R.drawable.ic_outline_push_pin_24)
                s.remove(mutableList[position].key)
                saveOrderedCollection(s,"presets")
            }
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    fun saveOrderedCollection(collection: MutableList<String>,key:String){
        val jsonArray =JSONArray(collection)
        sharedPreferences.edit().putString(key,jsonArray.toString()).apply()
    }

    fun loadOrderedCollection(key: String): MutableList<String> {
        val arrayList = mutableListOf<String>()
        val jsonArray =JSONArray(sharedPreferences.getString(key, "[]"))
        for (i in 0 until jsonArray.length()) {
            arrayList.add(jsonArray.get(i) as String)
        }
        return arrayList
    }

    override fun getItemCount(): Int {
        return mutableList.size
    }
}