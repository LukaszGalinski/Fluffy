package com.lukasz.galinski.fluffy.presentation.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.LoginHostLayoutBinding
import com.lukasz.galinski.fluffy.presentation.common.AppEntryPoint.LOGIN
import com.lukasz.galinski.fluffy.presentation.common.LoginEntryPoint
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AccountHostActivity : AppCompatActivity() {

    companion object {
        private const val ACCOUNT_INTENT_KEY = "ACCOUNT_KEY"

        fun createIntent(context: Context, entryPoint: LoginEntryPoint): Intent {
            return Intent(context, AccountHostActivity::class.java).apply {
                putExtra(ACCOUNT_INTENT_KEY, entryPoint.name)
            }
        }
    }

    private var _loginViewBinding: LoginHostLayoutBinding? = null
    private val loginViewBinding get() = _loginViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _loginViewBinding = LoginHostLayoutBinding.inflate(layoutInflater)
        setContentView(loginViewBinding.root)

        setToolbarOff()
        LoginEntryPoint.valueOf(intent.getStringExtra(ACCOUNT_INTENT_KEY) ?: LOGIN.name).apply {
            navigateBasedOnEntryPoint(this)
        }
    }

    private fun navigateBasedOnEntryPoint(intentLabel: LoginEntryPoint) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment

        with(navHostFragment.navController) {
            when (intentLabel) {
                LoginEntryPoint.REGISTER -> this.navigate(R.id.action_loginScreen_to_registerScreen)
                LoginEntryPoint.ONBOARDING -> this.navigate(R.id.action_loginScreen_to_onboardingParentFragment)
                LoginEntryPoint.LOGIN -> Unit
            }
        }
    }

    private fun setToolbarOff() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    override fun onDestroy() {
        _loginViewBinding = null
        super.onDestroy()
    }
}
