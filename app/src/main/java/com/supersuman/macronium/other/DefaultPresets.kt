package com.supersuman.macronium.other

import java.util.UUID

val defaultPresets: List<Preset> = listOf(
    Preset(UUID.fromString("1f1b440f-11be-41ed-b6bb-0fe91dcd5617"), "Show Desktop", mutableListOf(Keys.LeftSuper.name, Keys.D.name), false, false),
    Preset(UUID.fromString("ed110d2a-db36-415b-b102-2184e9df5078"), "Task view", mutableListOf(Keys.LeftSuper.name, Keys.Tab.name), false, false),
    Preset(UUID.fromString("eefdfa1e-dd4c-4c4f-a826-f3d212fe6ed4"), "Screenshot", mutableListOf(Keys.LeftSuper.name, Keys.Print.name), false, false),
    Preset(UUID.fromString("bd8cdd66-16e5-4f54-84e2-238f9918c2ad"), "Right", mutableListOf(Keys.Right.name), false, false),
    Preset(UUID.fromString("567c99d3-dba5-4569-bfb4-000051488c9a"), "Left", mutableListOf(Keys.Left.name), false, false)
)