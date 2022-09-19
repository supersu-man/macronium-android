package com.supersuman.macronium


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.supersuman.macronium.fragments.HomeFragment
import com.supersuman.macronium.fragments.MainFragment
import com.supersuman.macronium.fragments.MenuFragment
import com.supersuman.macronium.fragments.MouseFragment
import com.supersuman.macronium.other.BackgroundService

val fragments = listOf(MouseFragment(), HomeFragment(), MenuFragment())
val fragmentNames = listOf("Touch Pad", "Home", "Menu")

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpIntent()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, MainFragment())
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

}