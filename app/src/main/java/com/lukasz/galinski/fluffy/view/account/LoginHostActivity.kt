package com.lukasz.galinski.fluffy.view.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.createToast
import com.lukasz.galinski.fluffy.databinding.LoginHostLayoutBinding
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel

private const val ACCOUNT_TAG = "Account activity: "
private const val ACCOUNT_LOGIN = "Login screen"
private const val ACCOUNT_REGISTER = "REGISTER"
private const val BACK_BUTTON_DELAY = 1000L

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
    private var doubleCheckButton = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _loginViewBinding = LoginHostLayoutBinding.inflate(layoutInflater)
        setContentView(loginViewBinding.root)
        setToolbarOff()
        runnable = Runnable { doubleCheckButton = false }
        val chosenScreen = intent.getStringExtra(ACCOUNT_INTENT_KEY) ?: ""
        loadScreenFromOnboarding(chosenScreen)
    }

    private fun loadScreenFromOnboarding(intentLabel: String){
        if (intentLabel == ACCOUNT_REGISTER){
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navHostFragment.navController.navigate(R.id.action_loginScreen_to_registerScreen)
        }
    }

    private fun setToolbarOff() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    override fun onBackPressed() =
        when (findNavController(R.id.fragmentContainerView).graph.startDestination) {
            findNavController(R.id.fragmentContainerView).currentDestination?.id -> {
                createBackButtonDelay()
            }
            else -> super.onBackPressed()
        }

    private fun createBackButtonDelay() {
        if (doubleCheckButton) {
            super.onBackPressed()
            return
        }
        doubleCheckButton = true
        this.createToast(resources.getString(R.string.double_press_to_exit))
        handler.postDelayed(runnable, BACK_BUTTON_DELAY)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        _loginViewBinding = null
        super.onDestroy()
    }
}
