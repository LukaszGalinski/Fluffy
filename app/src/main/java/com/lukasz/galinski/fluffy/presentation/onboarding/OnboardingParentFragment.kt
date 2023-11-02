package com.lukasz.galinski.fluffy.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenLayoutBinding
import com.lukasz.galinski.fluffy.presentation.account.login.LoginViewModel
import com.lukasz.galinski.fluffy.presentation.common.AppEntryPoint
import com.lukasz.galinski.fluffy.presentation.common.handleBackPress
import com.lukasz.galinski.fluffy.presentation.common.logInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class OnboardingParentFragment : Fragment() {

    private lateinit var viewPager: ViewPager2
    private var _onboardingViewBinding: OnboardingScreenLayoutBinding? = null
    private val onboardingViewBinding get() = _onboardingViewBinding!!
    private val onboardingViewModel: OnboardingViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _onboardingViewBinding = OnboardingScreenLayoutBinding.inflate(layoutInflater)
        return onboardingViewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleBackPress { onboardingViewModel.swipeBackIfPossible() }
        observeUiEvent()
        buildViewPager()
        buildStepsIndication()
        setButtons()
    }

    private fun observeUiEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                onboardingViewModel.onboardingUiEvent.collect {
                    when (it) {
                        is OnboardingUiState.SwipeBack -> {
                            viewPager.currentItem += -1
                        }

                        is OnboardingUiState.Idle -> logInfo(it.toString())
                    }
                }
            }
        }
    }

    private fun setButtons() {
        onboardingViewBinding.onboardingLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingParentFragment_to_loginScreen)
            loginViewModel.setEntryPoint(AppEntryPoint.LOGIN)
        }

        onboardingViewBinding.onboardingRegisterButton.setOnClickListener {
            loginViewModel.setEntryPoint(AppEntryPoint.LOGIN)
            findNavController().navigate(R.id.action_onboardingParentFragment_to_registerScreen)
        }
    }

    private fun buildViewPager() {
        viewPager = onboardingViewBinding.onboardingFragmentHost
        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter

        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onboardingViewModel.updateCurrentPage(position)
            }
        })
    }

    private fun buildStepsIndication() {
        TabLayoutMediator(
            onboardingViewBinding.onboardingStepIndicator,
            viewPager
        ) { _, _ -> }.attach()
    }


    override fun onDestroy() {
        _onboardingViewBinding = null
        super.onDestroy()
    }
}
