package com.supersuman.macronium.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.card.MaterialCardView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.supersuman.macronium.R
import com.supersuman.macronium.fragmentNames
import com.supersuman.macronium.fragments

class MainFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var materialCardView : MaterialCardView
    private lateinit var viewPager: ViewPager2


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setupViewPager()
        setupTabLayout()

    }

    private fun initViews(view: View){
        tabLayout = view.findViewById(R.id.mainActivityTabLayout)
        viewPager = view.findViewById(R.id.mainActivityViewPager)
        materialCardView = view.findViewById(R.id.mainActivityButtonsParentCard)
    }

    private fun setupViewPager() {
        viewPager.adapter = PagerAdapter(requireActivity(), fragments)
        viewPager.currentItem = 1
        viewPager.offscreenPageLimit = 3
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = fragmentNames[position]
        }.attach()
        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    1 -> materialCardView.animate().translationY(0f)
                    else -> materialCardView.animate().translationY(materialCardView.height.toFloat()+50)
                }
            }
        })
    }

    private fun setupTabLayout() {}

}

class PagerAdapter(fragmentActivity: FragmentActivity, private val fragments : List<Fragment>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}