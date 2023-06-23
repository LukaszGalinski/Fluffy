package com.lukasz.galinski.fluffy.presentation.account.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding
import com.lukasz.galinski.fluffy.presentation.account.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.presentation.createToast
import com.lukasz.galinski.fluffy.presentation.main.MainMenuActivity
import com.lukasz.galinski.fluffy.presentation.setGone
import com.lukasz.galinski.fluffy.presentation.setVisible
import dagger.hilt.android.AndroidEntryPoint

private const val MARKED_SPANS_COUNT = 7
private const val HIGHLIGHTED_COLOR = "#7F3DFF"
private const val STATE_TAG = "LoginScreen"

@AndroidEntryPoint
class LoginScreen : Fragment() {
    private var _loginBinding: LoginScreenFragmentBinding? = null
    private val loginBinding get() = _loginBinding!!
    private val hostViewModel: LoginViewModel by activityViewModels()

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
        val registerLabel = loginBinding.createAccountInfo
        registerLabel.text = highlightSelectedTextRange(
            registerLabel.text,
            registerLabel.text.length - MARKED_SPANS_COUNT,
            registerLabel.text.length,
            HIGHLIGHTED_COLOR
        )

        loginBinding.createAccountInfo.setOnClickListener {
            findNavController(it).navigate(R.id.action_loginScreen_to_registerScreen)
        }

        loginBinding.loginButton.setOnClickListener {
            val userEmail = loginBinding.etLogin.text.toString()
            val userPassword = loginBinding.etPassword.text.toString()
            hostViewModel.loginUser(userEmail, userPassword)
        }
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
            Log.i(STATE_TAG, it.toString())
        }
    }

    private fun showToast(message: String) {
        requireContext().createToast(message)
    }

    override fun onDestroy() {
        super.onDestroy()
        _loginBinding = null
    }
}
