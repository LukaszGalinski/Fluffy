package com.lukasz.galinski.fluffy.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.lukasz.galinski.fluffy.presentation.account.AppEntryPoint
import com.lukasz.galinski.fluffy.presentation.account.LoginEntryPoint
import com.lukasz.galinski.fluffy.presentation.account.AccountHostActivity
import com.lukasz.galinski.fluffy.presentation.account.login.LoginViewModel
import com.lukasz.galinski.fluffy.presentation.main.MainMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        presentViewBasedOnEntryPoint()
    }

    private fun presentViewBasedOnEntryPoint() {
        lifecycleScope.launchWhenStarted {
            when (viewModel.getEntryPoint().first()) {
                AppEntryPoint.LOGIN -> startActivity(
                    AccountHostActivity.createIntent(applicationContext, LoginEntryPoint.LOGIN)
                )

                AppEntryPoint.ONBOARDING -> startActivity(
                    AccountHostActivity.createIntent(applicationContext, LoginEntryPoint.ONBOARDING)
                )

                AppEntryPoint.MAIN_MENU -> startActivity(
                    MainMenuActivity.createIntent(applicationContext)
                )
            }
            finishAfterTransition()
        }
    }

    override fun onBackPressed() {
        return
    }
}
