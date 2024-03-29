package com.lukasz.galinski.fluffy.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.lukasz.galinski.fluffy.presentation.account.AccountHostActivity
import com.lukasz.galinski.fluffy.presentation.account.login.LoginViewModel
import com.lukasz.galinski.fluffy.presentation.common.AppEntryPoint
import com.lukasz.galinski.fluffy.presentation.common.LoginEntryPoint
import com.lukasz.galinski.fluffy.presentation.main.MainMenuActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        presentViewBasedOnEntryPoint()
    }

    private fun presentViewBasedOnEntryPoint() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
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
    }

    override fun onBackPressed() {
        return
    }
}
