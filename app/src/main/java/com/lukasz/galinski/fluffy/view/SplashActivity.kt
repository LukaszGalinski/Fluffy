package com.lukasz.galinski.fluffy.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lukasz.galinski.fluffy.view.main.MainMenuActivity
import com.lukasz.galinski.fluffy.view.onboarding.OnBoardingScreenActivity
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

            if (loginStatus.first() != 0L) {
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
