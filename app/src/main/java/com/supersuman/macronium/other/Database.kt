package com.supersuman.macronium.other

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val presetsDatabaseName = "app_database"

@Entity(tableName = "presets")
class Preset {
    @PrimaryKey
    var presetName: String = ""
    var presetCommand: MutableList<String> = mutableListOf()
    var myPreset = false
    var pinned = false
}

@Dao
interface DatabaseDao {
    @Query("SELECT * FROM presets WHERE myPreset = 1")
    fun getMyPresets(): LiveData<MutableList<Preset>>

    @Query("SELECT * FROM presets WHERE pinned = 1")
    fun getPinnedPresets(): LiveData<MutableList<Preset>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPresets(preset: Preset)

    @Delete
    fun deletePreset(preset: Preset)
}

@TypeConverters(Converters::class)
@Database(entities = [Preset::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao
}

class Converters {
    @TypeConverter
    fun fromList(list: MutableList<String>) = Json.encodeToString(list)

    @TypeConverter
    fun toList(string: String) = Json.decodeFromString<MutableList<String>>(string)
}