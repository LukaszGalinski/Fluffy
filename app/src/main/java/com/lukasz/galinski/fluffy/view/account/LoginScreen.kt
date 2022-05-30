package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.createToast
import com.lukasz.galinski.fluffy.common.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.common.setInvisible
import com.lukasz.galinski.fluffy.common.setVisible
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding
import com.lukasz.galinski.fluffy.view.main.MainMenuActivity
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val MARKED_SPANS_COUNT = 7
private const val HIGHLIGHTED_COLOR = "#7F3DFF"
private const val STATE_TAG = "State: "

@AndroidEntryPoint
class LoginScreen : Fragment() {
    private var _loginBinding: LoginScreenFragmentBinding? = null
    private val loginBinding get() = _loginBinding!!
    private val hostViewModel: LoginViewModel by viewModels()

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
            findNavController(view).navigate(R.id.action_loginScreen_to_registerScreen)
        }

        loginBinding.loginButton.setOnClickListener {
            val userEmail = loginBinding.etLogin.text.toString()
            val userPassword = loginBinding.etPassword.text.toString()
            hostViewModel.loginUser(userEmail, userPassword)
        }
    }

    private fun handleLoginStates() = lifecycleScope.launchWhenStarted {
        hostViewModel.userAccountState.collect { state ->
            when (state) {
                is Success -> {
                    Log.i(STATE_TAG, state.toString())
                    hostViewModel.setLoggedUser(state.userId)
                    context?.let {
                        activity?.finishAndRemoveTask()
                        startActivity(MainMenuActivity.createIntent(it))
                    }
                }
                is Failure -> {
                    Log.i(STATE_TAG, state.toString())
                }
                is Loading -> {
                    Log.i(STATE_TAG, state.toString())
                    loginBinding.loginProgressBar.setVisible()
                }
                is Idle -> {
                    Log.i(STATE_TAG, state.toString())
                    loginBinding.loginProgressBar.setInvisible()
                }
                is UserNotFound -> {
                    Log.i(STATE_TAG, state.toString())
                    context?.createToast(resources.getString(R.string.user_not_found))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _loginBinding = null
    }
}
