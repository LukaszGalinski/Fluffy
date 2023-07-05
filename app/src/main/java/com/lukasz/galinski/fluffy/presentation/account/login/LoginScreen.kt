package com.lukasz.galinski.fluffy.presentation.account.login

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding
import com.lukasz.galinski.fluffy.presentation.common.createToast
import com.lukasz.galinski.fluffy.presentation.common.handleBackPress
import com.lukasz.galinski.fluffy.presentation.common.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.presentation.common.logInfo
import com.lukasz.galinski.fluffy.presentation.common.setGone
import com.lukasz.galinski.fluffy.presentation.common.setVisible
import com.lukasz.galinski.fluffy.presentation.main.MainMenuActivity
import dagger.hilt.android.AndroidEntryPoint

private const val MARKED_SPANS_COUNT = 7
private const val HIGHLIGHTED_COLOR = "#7F3DFF"
private const val STATE_TAG = "LoginScreen"
private const val BACK_BUTTON_DELAY = 1000L

@AndroidEntryPoint
class LoginScreen : Fragment() {
    private var _loginBinding: LoginScreenFragmentBinding? = null
    private val loginBinding get() = _loginBinding!!
    private val hostViewModel: LoginViewModel by activityViewModels()

    private var doubleCheckButton = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _loginBinding = LoginScreenFragmentBinding.inflate(inflater)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleLoginStates()
        handleBackPress { handleBackPress() }
        runnable = Runnable { doubleCheckButton = false }

        with(loginBinding.createAccountInfo) {
            text = highlightSelectedTextRange(
                text,
                text.length - MARKED_SPANS_COUNT,
                text.length,
                HIGHLIGHTED_COLOR
            )
        }

        loginBinding.createAccountInfo.setOnClickListener {
            findNavController(it).navigate(R.id.action_loginScreen_to_registerScreen)
        }

        loginBinding.loginButton.setOnClickListener {
            val userEmail = loginBinding.etLogin.text.toString()
            val userPassword = loginBinding.etPassword.text.toString()
            hostViewModel.loginUser(userEmail, userPassword)
        }
    }

    private fun handleBackPress() {
        when (findNavController().currentDestination?.id) {
            R.id.loginScreen -> createBackButtonDelay()
            R.id.registerScreen -> findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
            else -> findNavController().popBackStack()
        }
    }

    private fun createBackButtonDelay() {
        if (doubleCheckButton) {
            activity?.finishAndRemoveTask()
            return
        }
        doubleCheckButton = true
        requireContext().createToast(resources.getString(R.string.double_press_to_exit))
        handler.postDelayed(runnable, BACK_BUTTON_DELAY)
    }

    private fun handleLoginStates() = lifecycleScope.launchWhenStarted {
        hostViewModel.loginUiEvent.collect {
            when (it) {
                LoginUiState.LoginSuccess -> {
                    activity?.finish()
                    startActivity(MainMenuActivity.createIntent(requireContext()))
                }

                is LoginUiState.DisplayToast -> {
                    showToast(getString(R.string.unexpected_error_message))
                }

                is LoginUiState.IsLoading -> when (it.isLoading) {
                    true -> loginBinding.loginProgressBar.setVisible()
                    false -> loginBinding.loginProgressBar.setGone()
                }

                LoginUiState.UserNotFound -> {
                    showToast(getString(R.string.user_not_found))
                }

                LoginUiState.Idle -> Unit
            }
            logInfo(it.toString())
        }
    }

    private fun showToast(message: String) {
        requireContext().createToast(message)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        _loginBinding = null
        super.onDestroy()
    }
}
