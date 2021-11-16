package com.lukasz.galinski.fluffy.view

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenLayoutBinding

private const val ONBOARDING_PAGES = 3
class OnBoardingScreenActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingBinding: OnboardingScreenLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onboardingBinding = OnboardingScreenLayoutBinding.inflate(layoutInflater)
        setContentView(onboardingBinding.root)
        viewPager = onboardingBinding.onboardingFragmentHost
        buildViewPager()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = ONBOARDING_PAGES
        override fun createFragment(position: Int): Fragment = OnboardingFragment(position)
    }

    private fun buildViewPager(){
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) { super.onBackPressed() }
        else { viewPager.currentItem = viewPager.currentItem - 1 }
    }
}
