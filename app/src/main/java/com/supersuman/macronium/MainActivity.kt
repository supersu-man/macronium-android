package com.supersuman.macronium


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.supersuman.macronium.fragments.HomeFragment
import com.supersuman.macronium.fragments.MainFragment
import com.supersuman.macronium.fragments.MenuFragment
import com.supersuman.macronium.fragments.MouseFragment
import com.supersuman.macronium.other.AppDatabase
import com.supersuman.macronium.other.BackgroundService
import com.supersuman.macronium.other.Preset
import com.supersuman.macronium.other.presetsDatabaseName
import kotlin.concurrent.thread

val fragments = listOf(MouseFragment(), HomeFragment(), MenuFragment())
val fragmentNames = listOf("Touch Pad", "Home", "Menu")
lateinit var appDatabase: AppDatabase

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpIntent()
        setUpDb()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, MainFragment())
        transaction.commit()
    }

    private fun setUpIntent() {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                val link = intent.getStringExtra(Intent.EXTRA_TEXT)
                val serviceIntent = Intent(this, BackgroundService::class.java)
                serviceIntent.action = "SEND_MESSAGE"
                serviceIntent.putExtra("key", "open-link")
                serviceIntent.putExtra("arg", link)
                startService(serviceIntent)
            }
        }
    }

    private fun setUpDb(){
        appDatabase = Room.databaseBuilder(this, AppDatabase::class.java, presetsDatabaseName).build()
    }

}