package com.supersuman.macronium.other

import android.content.SharedPreferences
import org.json.JSONArray

val data : MutableList<Preset> = mutableListOf(
    Preset().also {
        it.presetName = "Show Desktop"
        it.presetCommand = mutableListOf(Keys.LeftSuper.name, Keys.D.name)
    },
    Preset().also {
        it.presetName = "Task view"
        it.presetCommand = mutableListOf(Keys.LeftSuper.name, Keys.Tab.name)
    },
    Preset().also {
        it.presetName = "Screenshot"
        it.presetCommand = mutableListOf(Keys.LeftSuper.name, Keys.Print.name)
    },
    Preset().also {
        it.presetName = "Stop"
        it.presetCommand = mutableListOf(Keys.AudioStop.name)
    },
    Preset().also {
        it.presetName = "Play"
        it.presetCommand = mutableListOf(Keys.AudioPlay.name)
    },
    Preset().also {
        it.presetName = "Pause"
        it.presetCommand = mutableListOf(Keys.AudioPause.name)
    },
    Preset().also {
        it.presetName = "Mute"
        it.presetCommand = mutableListOf(Keys.AudioMute.name)
    },
    Preset().also {
        it.presetName = "Volume down"
        it.presetCommand = mutableListOf(Keys.AudioVolDown.name)
    },
    Preset().also {
        it.presetName = "Volume up"
        it.presetCommand = mutableListOf(Keys.AudioVolUp.name)
    },
    Preset().also {
        it.presetName = "Previous track"
        it.presetCommand = mutableListOf(Keys.AudioPrev.name)
    },
    Preset().also {
        it.presetName = "Next track"
        it.presetCommand = mutableListOf(Keys.LeftSuper.name, Keys.LeftControl.name, Keys.D.name)
    },
    Preset().also {
        it.presetName = "Right"
        it.presetCommand = mutableListOf(Keys.Right.name)
    },
    Preset().also {
        it.presetName = "Left"
        it.presetCommand = mutableListOf(Keys.Left.name)
    }
)

fun loadOrderedCollection(sharedPreferences: SharedPreferences ,key: String): MutableList<MutableList<String>> {
    val mutableList : MutableList<MutableList<String>> = mutableListOf()
    val string = sharedPreferences.getString(key, null) ?: return mutableList
    val jsonArray = JSONArray(string)
    for (i in 0 until jsonArray.length()) {
        val tempString = jsonArray.getString(i)
        val tempArray = JSONArray(tempString)
        val tempList : MutableList<String> = mutableListOf()
        for (j in 0 until tempArray.length()){
            tempList.add(tempArray.getString(j))
        }
        mutableList.add(tempList)
    }
    return mutableList
}

fun saveOrderedCollection(sharedPreferences: SharedPreferences, mutableList: MutableList<MutableList<String>>, key:String){
    val jsonArray = JSONArray()
    for (item in mutableList){
        val tempArray = JSONArray()
        for (i in item){
            tempArray.put(i)
        }
        jsonArray.put(tempArray)
    }
    sharedPreferences.edit().putString(key,jsonArray.toString()).apply()
}

fun getSearchResults(string: String): MutableList<MutableList<String>> {
    val mutableList = mutableListOf<MutableList<String>>()
    for(item in data){
        if (string.lowercase() in item.toString().lowercase()){
            //mutableList.add(item)
        }
    }
    return mutableList
}
