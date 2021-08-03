package com.supersuman.macronium


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.card.MaterialCardView
import com.iammert.library.AnimatedTabLayout


class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: AnimatedTabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var materialCardView : MaterialCardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupTabLayout()
        setupViewPager()
    }



    private fun setupTabLayout() {
        tabLayout.setupViewPager(viewPager)
        tabLayout.setTabChangeListener(object : AnimatedTabLayout.OnChangeListener{
            override fun onChanged(position: Int) {
                when(position){
                    0 -> {
                        materialCardView.animate().translationY(0f)
                    }
                    1 -> materialCardView.animate().translationY(materialCardView.height.toFloat()+50)
                }
            }
        })
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(supportFragmentManager)
    }

    private fun initViews(){
        tabLayout = findViewById(R.id.mainActivityTabLayout)
        viewPager = findViewById(R.id.MainActivityViewPager)
        materialCardView = findViewById(R.id.mainActivityButtonsParentCard)
    }

}