package com.lukasz.galinski.fluffy.view.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.LoginHostLayoutBinding

private const val ACCOUNT_TAG = "Account activity: "
private const val ACCOUNT_LOGIN = "Login screen"
private const val ACCOUNT_REGISTER = "Register screen"

class LoginHostActivity : AppCompatActivity() {

    companion object {
        private const val ACCOUNT_INTENT_KEY = "ACCOUNT_KEY"

        fun createIntent(context: Context, chosenScreen: String): Intent {
            return Intent(context, LoginHostActivity::class.java).apply {
                putExtra(ACCOUNT_INTENT_KEY, chosenScreen)
            }
        }
    }

    private var _loginViewBinding: LoginHostLayoutBinding? = null
    private val loginViewBinding get() = _loginViewBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _loginViewBinding = LoginHostLayoutBinding.inflate(layoutInflater)
        setContentView(loginViewBinding.root)
        val chosenScreen = intent.getStringExtra(ACCOUNT_INTENT_KEY) ?: ""
        loadDefaultDestination(savedInstanceState, chosenScreen)
    }

    private fun loadDefaultDestination(savedState: Bundle?, chosenScreen: String){

    }

    override fun onDestroy() {
        _loginViewBinding = null
        super.onDestroy()
    }
}
