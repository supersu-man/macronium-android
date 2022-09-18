package com.supersuman.macronium.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.supersuman.macronium.R
import com.supersuman.macronium.adapters.PagerAdapter
import com.supersuman.macronium.fragmentNames
import com.supersuman.macronium.fragments

class MainFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var materialCardView : MaterialCardView
    private lateinit var viewPager: ViewPager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        setupViewPager()
        setupTabLayout()

    }

    private fun initViews(){
        tabLayout = requireActivity().findViewById(R.id.mainActivityTabLayout)
        viewPager = requireActivity().findViewById(R.id.MainActivityViewPager)
        materialCardView = requireActivity().findViewById(R.id.mainActivityButtonsParentCard)
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(childFragmentManager, fragments, fragmentNames)
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
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