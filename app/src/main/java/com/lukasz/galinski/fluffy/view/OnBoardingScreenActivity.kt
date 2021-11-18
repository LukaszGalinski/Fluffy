package com.lukasz.galinski.fluffy.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenLayoutBinding

private const val ONBOARDING_PAGES = 3
class OnBoardingScreenActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private var _onboardingViewBinding: OnboardingScreenLayoutBinding? = null
    private val onboardingViewBinding get() = _onboardingViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _onboardingViewBinding = OnboardingScreenLayoutBinding.inflate(layoutInflater)
        setContentView(onboardingViewBinding.root)
        buildViewPager()
        buildStepsIndication()
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = ONBOARDING_PAGES
        override fun createFragment(position: Int): Fragment = OnboardingFragment(position)
    }

    private fun buildViewPager() {
        viewPager = onboardingViewBinding.onboardingFragmentHost
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
    }

    private fun buildStepsIndication() {
        TabLayoutMediator(
            onboardingViewBinding.onboardingStepIndicator,
            viewPager
        ) { _, _ -> }.attach()
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == 0) {
            super.onBackPressed()
        } else {
            viewPager.currentItem = viewPager.currentItem - 1
        }
    }

    override fun onDestroy() {
        _onboardingViewBinding = null
        super.onDestroy()
    }
}
