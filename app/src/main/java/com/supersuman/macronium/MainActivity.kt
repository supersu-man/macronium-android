package com.supersuman.macronium


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var materialCardView : MaterialCardView
    private lateinit var viewPager: ViewPager

    val fragments = listOf(MouseFragment(), HomeFragment(), PresetsFragment())
    val fragmentNames = listOf("Touch Pad", "Home", "Presets")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpIntent()
        initViews()
        setupViewPager()
        setupTabLayout()

    }

    private fun setUpIntent() {
        when (intent.action) {
            Intent.ACTION_SEND -> {
                val link = intent.getStringExtra(Intent.EXTRA_TEXT)
                val intent = Intent(this, BackgroundService::class.java)
                intent.action = "SEND_MESSAGE"
                intent.putExtra("message","Macronium-link <$link>")
                startService(intent)
            }
        }
    }

    private fun initViews(){
        tabLayout = findViewById(R.id.mainActivityTabLayout)
        viewPager = findViewById(R.id.MainActivityViewPager)
        materialCardView = findViewById(R.id.mainActivityButtonsParentCard)
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(supportFragmentManager, fragments, fragmentNames)
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                when (position){
                    1 -> materialCardView.animate().translationY(0f)
                    else -> materialCardView.animate().translationY(materialCardView.height.toFloat()+50)
                }
            }
        })
    }

    private fun setupTabLayout() {
        tabLayout.setupWithViewPager(viewPager)
    }

}