package com.supersuman.macronium


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.iammert.library.AnimatedTabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: AnimatedTabLayout
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        setupTabLayout()
        setupViewPager()
    }

    private fun setupTabLayout() {
        tabLayout.setupViewPager(viewPager)
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(supportFragmentManager)
    }

    private fun initViews(){
        tabLayout = findViewById(R.id.mainActivityTabLayout)
        viewPager = findViewById(R.id.MainActivityViewPager)
    }

}