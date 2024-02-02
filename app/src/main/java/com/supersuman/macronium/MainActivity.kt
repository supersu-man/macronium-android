package com.supersuman.macronium


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.room.Room
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.supersuman.macronium.fragments.HomeFragment
import com.supersuman.macronium.fragments.MediaFragment
import com.supersuman.macronium.fragments.MenuFragment
import com.supersuman.macronium.fragments.MouseFragment
import com.supersuman.macronium.other.AppDatabase
import com.supersuman.macronium.other.databaseName
import com.supersuman.macronium.other.defaultPresets
import com.supersuman.macronium.services.SocketService
import com.supersuman.macronium.services.address
import com.supersuman.macronium.services.socket
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var materialCardView: LinearLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var connectButton: MaterialCardView
    private lateinit var disconnectButton: MaterialCardView

    private val scanQrCode = registerForActivityResult(ScanQRCode(), ::handleResult)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tabLayout = findViewById(R.id.mainActivityTabLayout)
        viewPager = findViewById(R.id.mainActivityViewPager)
        materialCardView = findViewById(R.id.mainActivityButtonsParentCard)
        connectButton = findViewById(R.id.connectButton)
        disconnectButton = findViewById(R.id.disconnectButton)
        com.supersuman.macronium.services.connectButton = connectButton
        com.supersuman.macronium.services.disconnectButton = disconnectButton
        setupViewPager()

        if(socket?.connected() == true) {
            connectButton.visibility = View.GONE
            disconnectButton.visibility = View.VISIBLE
        } else {
            connectButton.visibility = View.VISIBLE
            disconnectButton.visibility = View.GONE
        }

        connectButton.setOnClickListener {
            scanQrCode.launch(null)
        }
        disconnectButton.setOnClickListener {
            try {
                stopService(Intent(this, SocketService::class.java))
            } catch (e: Exception) { }
        }

        //pre populate database with default presets
        val database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, databaseName).build()
        val dao = database.presetDao()
        thread {
            val storedDefaultPresets = dao.getDefaultPresets()
            if (storedDefaultPresets.map { it.uid } != defaultPresets.map { it.uid }) {
                dao.deletePresets(storedDefaultPresets)
                dao.insertPresets(defaultPresets)
            }
            println(dao.getDefaultPresets())
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }

    }



    private fun handleResult(result: QRResult) {
        if (result is QRResult.QRSuccess && result.content.rawValue != null) {
            val ipAddress = result.content.rawValue!!
            address = ipAddress
            val serviceIntent = Intent(this, SocketService::class.java)
            startService(serviceIntent)
        }
    }


    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(this, listOf(HomeFragment(), MediaFragment(), MouseFragment(), MenuFragment()))
        viewPager.offscreenPageLimit = 4
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = listOf( "Home", "Media", "Touch Pad", "Menu")[position]
        }.attach()
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> materialCardView.animate().translationY(0f)
                    else -> materialCardView.animate()
                        .translationY(materialCardView.height.toFloat() + 50)
                }
            }
        })
    }
}

class PagerAdapter(fragmentActivity: FragmentActivity, private val fragments: List<Fragment>) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int { return fragments.size }

    override fun createFragment(position: Int): Fragment { return fragments[position] }
}