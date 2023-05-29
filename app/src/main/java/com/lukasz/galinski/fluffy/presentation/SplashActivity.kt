package com.lukasz.galinski.fluffy.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lukasz.galinski.fluffy.presentation.main.MainMenuActivity
import com.lukasz.galinski.fluffy.presentation.onboarding.OnBoardingScreenActivity
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        lifecycleScope.launchWhenStarted {
            val loginStatus = viewModel.getLoggedUser()

            if (loginStatus.first() != null) {
                startActivity(
                    MainMenuActivity.createIntent(
                        applicationContext
                    )
                )
            } else {
                startActivity(OnBoardingScreenActivity.createIntent(applicationContext))
            }
            finishAfterTransition()
        }
    }

    override fun onBackPressed() {
        return
    }
}
