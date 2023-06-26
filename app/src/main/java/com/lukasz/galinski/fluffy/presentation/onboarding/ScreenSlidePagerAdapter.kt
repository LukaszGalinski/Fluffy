package com.lukasz.galinski.fluffy.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lukasz.galinski.core.data.OnboardingDetailsModel
import com.lukasz.galinski.fluffy.R

private const val ONBOARDING_PAGES = 3

class ScreenSlidePagerAdapter(private val fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = ONBOARDING_PAGES
    override fun createFragment(position: Int): Fragment =
        OnboardingFragment(getFragmentDetails(position))

    private fun getFragmentDetails(screenNumber: Int): OnboardingDetailsModel {
        return when (screenNumber) {
            0 -> OnboardingDetailsModel(
                title = fragment.getString(R.string.onBoarding_one_title),
                subtitle = fragment.getString(R.string.onBoarding_one_subtitle),
                logoImage = R.drawable.onboarding_one_logo
            )
            1 -> OnboardingDetailsModel(
                title = fragment.getString(R.string.onBoarding_two_title),
                subtitle = fragment.getString(R.string.onBoarding_two_subtitle),
                logoImage = R.drawable.onboarding_two_logo
            )
            else -> OnboardingDetailsModel(
                title = fragment.getString(R.string.onBoarding_three_title),
                subtitle = fragment.getString(R.string.onBoarding_three_subtitle),
                logoImage = R.drawable.onboarding_three_logo
            )
        }
    }
}