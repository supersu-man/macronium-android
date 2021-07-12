package com.supersuman.macronium

import java.util.*
import kotlin.collections.HashMap

class PresetsData {
    //Display Name , simplified shortcut combo, pynput command
    val data :MutableList<MutableList<String>> = mutableListOf(
        mutableListOf("Screenshot","Win+PrtSc","Key.cmd+Key.print_screen"),
        mutableListOf("Copy","Ctrl+C","Key.ctrl+c"),
        mutableListOf("Cut","Ctrl+X","Key.ctrl+x"),
        mutableListOf("Paste","Ctrl+V","Key.ctrl+v"),
        mutableListOf("Play/Pause","","Key.media_play_pause"),
        mutableListOf("Mute","","Key.media_volume_mute"),
        mutableListOf("Volume Down","","Key.media_volume_down"),
        mutableListOf("Volume Up","","Key.media_volume_up"),
        mutableListOf("Previous Track","","Key.media_previous"),
        mutableListOf("Next Track","","Key.media_next"),
        mutableListOf("F13","","Key.f13"),
        mutableListOf("F14","","Key.f14"),
        mutableListOf("F15","","Key.f15"),
        mutableListOf("F16","","Key.f16"),
        mutableListOf("F17","","Key.f17"),
        mutableListOf("F18","","Key.f18"),
        mutableListOf("F19","","Key.f19"),
        mutableListOf("F20","","Key.f20"),
        mutableListOf("F21","","Key.f21"),
        mutableListOf("F22","","Key.f22"),
        mutableListOf("F23","","Key.f23"),
        mutableListOf("F24","","Key.f24")
    )
    fun getMappedData(string: String): String? {
        val v = HashMap<String,String>()
        for (i in data){
            v[i[2]]=i[0]
        }
        return v[string]
    }
    fun getSearchResults(string: String): MutableList<MutableList<String>> {
        val v = mutableListOf<MutableList<String>>()

        for(i in data){
            if (string.lowercase(Locale.getDefault()) in i[0].lowercase(Locale.getDefault()) || string in i[1].lowercase(Locale.getDefault())){
                v.add(i)
            }
        }
        return v
    }

}