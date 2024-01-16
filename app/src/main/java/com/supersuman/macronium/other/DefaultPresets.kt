package com.supersuman.macronium.other

import java.util.UUID

val defaultPresets: List<Preset> = listOf(
    Preset(UUID.fromString("1f1b440f-11be-41ed-b6bb-0fe91dcd5617"), "Show Desktop", mutableListOf(Keys.LeftSuper.name, Keys.D.name), false, false),
    Preset(UUID.fromString("ed110d2a-db36-415b-b102-2184e9df5078"), "Task view", mutableListOf(Keys.LeftSuper.name, Keys.Tab.name), false, false),
    Preset(UUID.fromString("eefdfa1e-dd4c-4c4f-a826-f3d212fe6ed4"), "Screenshot", mutableListOf(Keys.LeftSuper.name, Keys.Print.name), false, false),
    Preset(UUID.fromString("3e673c15-18a1-47c6-a745-788e56bc441f"), "Stop", mutableListOf(Keys.AudioStop.name), false, false),
    Preset(UUID.fromString("752f6936-86e1-4f1d-9b37-dddb1c6dcbb6"), "Play", mutableListOf(Keys.AudioPlay.name), false, false),
    Preset(UUID.fromString("a6360004-ef1c-489b-8496-56c305ef4482"), "Pause", mutableListOf(Keys.AudioPause.name), false, false),
    Preset(UUID.fromString("7a9ba525-535f-457b-9bfc-7031a47b60fe"), "Mute", mutableListOf(Keys.AudioMute.name), false, false),
    Preset(UUID.fromString("346ab72b-0030-42df-847f-33bb56028152"), "Volume down", mutableListOf(Keys.AudioVolDown.name), false, false),
    Preset(UUID.fromString("7fd5e97e-56e0-4e63-bb7f-8d85643ad81c"), "Volume up", mutableListOf(Keys.AudioVolUp.name), false, false),
    Preset(UUID.fromString("660046e9-2839-4344-b8ac-e23a630ec5ac"), "Previous track", mutableListOf(Keys.AudioPrev.name), false, false),
    Preset(UUID.fromString("9ca1d3db-e926-4b63-a825-900f775402e9"), "Next track", mutableListOf(Keys.LeftSuper.name, Keys.LeftControl.name, Keys.D.name), false, false),
    Preset(UUID.fromString("bd8cdd66-16e5-4f54-84e2-238f9918c2ad"), "Right", mutableListOf(Keys.Right.name), false, false),
    Preset(UUID.fromString("567c99d3-dba5-4569-bfb4-000051488c9a"), "Left", mutableListOf(Keys.Left.name), false, false)
)