package com.supersuman.macronium

import java.util.*
import kotlin.collections.HashMap

class Preset(val displayName : String, val key : String)

class PresetsData {
    val data :MutableList<Preset> = mutableListOf(
        Preset("Space", "Space"),
        Preset("Tab", "Tab"),
        Preset("Escape", "Escape"),
        Preset("Alt", "LeftAlt"),
        Preset("Control", "LeftControl"),
        Preset("Shift", "LeftShift"),
        Preset("Super", "LeftSuper"),
        Preset("Enter", "Enter"),
        Preset("Return", "Return"),
        Preset("Home","Home"),
        Preset("End","End"),
        Preset("PageUp","PageUp"),
        Preset("PageDown","PageDown"),
        Preset("Print","Print"),
        Preset("Stop","AudioStop"),
        Preset("Play","AudioPlay"),
        Preset("Pause","AudioPause"),
        Preset("Mute","AudioMute"),
        Preset("Volume Down","AudioVolDown"),
        Preset("Volume Up","AudioVolUp"),
        Preset("Previous Track","AudioPrev"),
        Preset("Next Track","AudioNext"),
        Preset("Right","Right"),
        Preset("Left","Left"),
        Preset("Up","Up"),
        Preset("Down","Down")
    )

    fun getMappedData(string: String): String? {
        val v = HashMap<String,String>()
        for (i in data){
            v[i.key]=i.displayName
        }
        return v[string]
    }

    fun getSearchResults(string: String): MutableList<Preset> {
        val v = mutableListOf<Preset>()
        for(i in data){
            if (string.lowercase(Locale.getDefault()) in i.displayName.lowercase(Locale.getDefault())){
                v.add(i)
            }
        }
        return v
    }
}