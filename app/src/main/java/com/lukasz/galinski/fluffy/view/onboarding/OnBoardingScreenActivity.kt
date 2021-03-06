package com.lukasz.galinski.fluffy.view.onboarding

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.lukasz.galinski.fluffy.databinding.OnboardingScreenLayoutBinding
import com.lukasz.galinski.fluffy.view.account.LoginHostActivity
import com.lukasz.galinski.fluffy.view.main.MainMenuActivity
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

private const val REGISTER_SCREEN_LABEL = "REGISTER"

@AndroidEntryPoint
class OnBoardingScreenActivity : FragmentActivity() {

    private lateinit var viewPager: ViewPager2
    private var _onboardingViewBinding: OnboardingScreenLayoutBinding? = null
    private val onboardingViewBinding get() = _onboardingViewBinding!!
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _onboardingViewBinding = OnboardingScreenLayoutBinding.inflate(layoutInflater)
        setContentView(onboardingViewBinding.root)
        checkLoginStatus()
        buildViewPager()
        buildStepsIndication()
        setButtons()
    }

    private fun checkLoginStatus(){
        lifecycleScope.launchWhenStarted {
            val loginStatus = viewModel.getLoggedUser()
            if (loginStatus.first() != 0L){
                finish()
                startActivity(MainMenuActivity.createIntent(applicationContext, loginStatus.first()))
            }
        }
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
