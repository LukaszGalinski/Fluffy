package com.lukasz.galinski.fluffy.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lukasz.galinski.fluffy.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment(private val onboardingScreenNumber: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val onboardingScreenNumber = createViewBasedOnNumber(onboardingScreenNumber)
        return inflater.inflate(onboardingScreenNumber, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagerImage = view.findViewById<ImageView>(R.id.onboarding_logo)
        Glide.with(this).load(getViewPagerImage(onboardingScreenNumber))
            .centerCrop()
            .into(pagerImage)
    }
}

private fun createViewBasedOnNumber(screenNumber: Int): Int {
    return when (screenNumber) {
        1 -> R.layout.onboarding_screen_second_fragment
        2 -> R.layout.onboarding_screen_third_fragment
        else -> R.layout.onboarding_screen_first_fragment
    }
}

private fun getViewPagerImage(screenNumber: Int): Int {
    return when (screenNumber) {
        1 -> R.drawable.onboarding_two_logo
        2 -> R.drawable.onboarding_three_logo
        else -> R.drawable.onboarding_one_logo
    }
}