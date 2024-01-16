package com.supersuman.macronium.other

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.Update
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.UUID

const val databaseName = "com.supersuman.macronium"

@Entity(tableName = "presets")
data class Preset(
    @PrimaryKey val uid: UUID,
    var title: String,
    var command: MutableList<String>,
    var isCustom: Boolean,
    var isPinned: Boolean
)

@Dao
interface PresetsDao {
    @Query("SELECT * FROM presets WHERE isCustom = 1")
    fun getCustomPresets(): List<Preset>

    @Query("SELECT * FROM presets WHERE isCustom = 0")
    fun getDefaultPresets(): List<Preset>

    @Query("SELECT * FROM presets WHERE isPinned = 1")
    fun getPinnedPresets(): List<Preset>

    @Insert
    fun insertPreset(preset: Preset)

    @Insert
    fun insertPresets(presets: List<Preset>)

    @Update
    fun updatePreset(preset: Preset)

    @Delete
    fun deletePresets(presets: List<Preset>)

    @Delete
    fun deletePreset(preset: Preset)
}

class Converters {
    @TypeConverter
    fun fromList(list: MutableList<String>) = Json.encodeToString(list)
    @TypeConverter
    fun toList(string: String) = Json.decodeFromString<MutableList<String>>(string)
}
@TypeConverters(Converters::class)
@Database(entities = [Preset::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun presetDao(): PresetsDao
}