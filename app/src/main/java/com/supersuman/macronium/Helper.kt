package com.supersuman.macronium

import android.content.SharedPreferences
import org.json.JSONArray
import java.util.*

val data :MutableList<MutableList<String>> = mutableListOf(
    mutableListOf("Space", "Space"),
    mutableListOf("Tab", "Tab"),
    mutableListOf("Escape", "Escape"),
    mutableListOf("Alt", "LeftAlt"),
    mutableListOf("Control", "LeftControl"),
    mutableListOf("Shift", "LeftShift"),
    mutableListOf("Super", "LeftSuper"),
    mutableListOf("Enter", "Enter"),
    mutableListOf("Return", "Return"),
    mutableListOf("Home","Home"),
    mutableListOf("End","End"),
    mutableListOf("PageUp","PageUp"),
    mutableListOf("PageDown","PageDown"),
    mutableListOf("Print","Print"),
    mutableListOf("Stop","AudioStop"),
    mutableListOf("Play","AudioPlay"),
    mutableListOf("Pause","AudioPause"),
    mutableListOf("Mute","AudioMute"),
    mutableListOf("Volume Down","AudioVolDown"),
    mutableListOf("Volume Up","AudioVolUp"),
    mutableListOf("Previous Track","AudioPrev"),
    mutableListOf("Next Track","AudioNext"),
    mutableListOf("Right","Right"),
    mutableListOf("Left","Left"),
    mutableListOf("Up","Up"),
    mutableListOf("Down","Down")
)

fun listToString(mutableList: MutableList<String>) : String{
    val jsonArray = JSONArray()
    for (i in mutableList){
        jsonArray.put(i)
    }
    return jsonArray.toString()
}

fun stringToList(string: String): MutableList<String> {
    val jsonArray = JSONArray(string)
    val mutableList : MutableList<String> = mutableListOf()
    for (i in 0 until jsonArray.length()){
        mutableList.add(jsonArray.getString(i))
    }
    return mutableList
}

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
    for(i in data){
        if (string.lowercase() in i.toString()){
            mutableList.add(i)
        }
    }
    return mutableList
}
