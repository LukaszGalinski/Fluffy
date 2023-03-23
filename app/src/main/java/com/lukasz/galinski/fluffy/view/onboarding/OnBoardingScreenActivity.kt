package com.lukasz.galinski.fluffy.view.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenLayoutBinding
import com.lukasz.galinski.fluffy.view.account.login.LoginHostActivity
import dagger.hilt.android.AndroidEntryPoint

private const val REGISTER_SCREEN_LABEL = "REGISTER"

@AndroidEntryPoint
class OnBoardingScreenActivity : FragmentActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, OnBoardingScreenActivity::class.java)
        }
    }

    private lateinit var viewPager: ViewPager2
    private var _onboardingViewBinding: OnboardingScreenLayoutBinding? = null
    private val onboardingViewBinding get() = _onboardingViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        _onboardingViewBinding = OnboardingScreenLayoutBinding.inflate(layoutInflater)
        setContentView(onboardingViewBinding.root)
        buildViewPager()
        buildStepsIndication()
        setButtons()
    }

    private fun setButtons() {
        onboardingViewBinding.onboardingLoginButton.setOnClickListener {
            moveToAccountScreen()
        }

        onboardingViewBinding.onboardingRegisterButton.setOnClickListener {
            moveToAccountScreen(REGISTER_SCREEN_LABEL)
        }
    }

    private fun moveToAccountScreen(intentLabel: String = "") {
        finish()
        val intent = LoginHostActivity.createIntent(baseContext, intentLabel)
        startActivity(intent)
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
