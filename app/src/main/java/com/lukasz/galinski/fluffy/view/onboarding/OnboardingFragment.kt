package com.lukasz.galinski.fluffy.view.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lukasz.galinski.fluffy.R

class OnboardingFragment(private val onboardingScreenNumber: Int) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val onboardingScreenNumber = createViewBasedOnNumber(onboardingScreenNumber)
        return inflater.inflate(onboardingScreenNumber, container, false)
    }
}

private fun createViewBasedOnNumber(screenNumber: Int): Int {
    return when (screenNumber){
        1 -> R.layout.onboarding_screen_second_fragment
        2 -> R.layout.onboarding_screen_third_fragment
        else -> R.layout.onboarding_screen_first_fragment
    }
}