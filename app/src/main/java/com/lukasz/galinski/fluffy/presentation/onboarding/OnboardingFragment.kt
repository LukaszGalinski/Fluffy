package com.lukasz.galinski.fluffy.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenFirstFragmentBinding

class OnboardingFragment : Fragment() {
    private var _onboardingBinding: OnboardingScreenFirstFragmentBinding? = null
    private val onboardingBinding get() = _onboardingBinding!!

    companion object {
        private var fragmentDetails: OnboardingDetailsModel? = null

        fun create(details: OnboardingDetailsModel): OnboardingFragment {
            return OnboardingFragment().apply {
                fragmentDetails = details
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _onboardingBinding = OnboardingScreenFirstFragmentBinding.inflate(inflater)
        return onboardingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(fragmentDetails!!) {
            Glide.with(requireContext()).load(logoImage)
                .centerCrop()
                .into(onboardingBinding.onboardingLogo)

            onboardingBinding.onboardingTitle.text = title
            onboardingBinding.onboardingSubTitle.text = subtitle
            onboardingBinding.onboardingLogo.tag = logoImage
        }
    }
}