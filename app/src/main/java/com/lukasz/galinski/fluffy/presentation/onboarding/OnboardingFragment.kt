package com.lukasz.galinski.fluffy.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenFirstFragmentBinding
import com.lukasz.galinski.core.data.OnboardingDetailsModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment(private val fragmentDetails: OnboardingDetailsModel) : Fragment() {
    private var _onboardingBinding: OnboardingScreenFirstFragmentBinding? = null
    private val onboardingBinding get() = _onboardingBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _onboardingBinding = OnboardingScreenFirstFragmentBinding.inflate(inflater)
        return onboardingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Glide.with(this).load(fragmentDetails.logoImage)
            .centerCrop()
            .into(onboardingBinding.onboardingLogo)
        onboardingBinding.onboardingTitle.text = fragmentDetails.title
        onboardingBinding.onboardingSubTitle.text = fragmentDetails.subtitle
        onboardingBinding.onboardingLogo.tag = fragmentDetails.logoImage
    }
}