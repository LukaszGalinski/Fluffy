package com.lukasz.galinski.fluffy.presentation.account.register

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.core.data.User
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.RegisterScreenFragmentBinding
import com.lukasz.galinski.fluffy.presentation.account.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.presentation.createToast
import com.lukasz.galinski.fluffy.presentation.markAs
import com.lukasz.galinski.fluffy.presentation.setGone
import com.lukasz.galinski.fluffy.presentation.setStateAppearance
import com.lukasz.galinski.fluffy.presentation.setVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterScreen : Fragment() {

    companion object {
        private const val HIGHLIGHTED_TERMS_SPANS_COUNT = 35
        private const val HIGHLIGHTED_LOGIN_SPANS_COUNT = 5
        private const val HIGHLIGHTED_COLOR = "#7F3DFF"
        private const val STATE_TAG = "RegisterScreen"
    }

    private var _registerBinding: RegisterScreenFragmentBinding? = null
    private val registerBinding get() = _registerBinding!!
    private val viewModel by activityViewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _registerBinding = RegisterScreenFragmentBinding.inflate(inflater)
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        highlightTextParts()
        assignValidation()
        handleRegisterStates()
        observeRegisterButton()

        registerBinding.termsCheckbox.setOnClickListener {
            registerBinding.etName.text = registerBinding.etName.text
        }

        registerBinding.existingAccountInfo.setOnClickListener {
            navigateToLogin()
        }

        registerBinding.registerButton.setOnClickListener {
            val userEmail = registerBinding.etLogin.text.toString()
            val userPassword = registerBinding.etPassword.text.toString()
            val userName = registerBinding.etName.text.toString()
            viewModel.saveUserIntoDatabase(User(userName, userEmail, userPassword, "9999"))
        }
    }

    private fun observeRegisterButton() = lifecycleScope.launchWhenStarted {
        viewModel.saveButtonState.collect {
            registerBinding.registerButton.markAs(it)
            registerBinding.registerButton.setStateAppearance()
        }
    }

    private fun handleRegisterStates() = lifecycleScope.launchWhenStarted {
        viewModel.registerUiEvent.collect { state ->
            when (state) {
                is RegisterUiEvent.RegisterSuccess -> {
                    showToast(getString(R.string.user_created))
                    navigateToLogin()
                }

                is RegisterUiEvent.DisplayToast -> {
                    Log.i(STATE_TAG, state.exception.message.toString())
                    showToast(getString(R.string.email_occupied))
                }

                is RegisterUiEvent.IsLoading -> when (state.isLoading) {
                    true -> registerBinding.registerProgressBar.setVisible()
                    false -> registerBinding.registerProgressBar.setGone()
                }

                is RegisterUiEvent.Idle -> Unit

            }
            Log.i(STATE_TAG, state.toString())
        }
    }

    private fun showToast(message: String) {
        requireContext().createToast(message)
    }

    private fun navigateToLogin(){
        findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
    }

    private fun highlightTextParts() {
        val checkBoxText = registerBinding.termsCheckbox
        val loginLabel = registerBinding.existingAccountInfo
        checkBoxText.text = highlightSelectedTextRange(
            checkBoxText.text,
            checkBoxText.text.length - HIGHLIGHTED_TERMS_SPANS_COUNT,
            checkBoxText.text.length,
            HIGHLIGHTED_COLOR
        )
        loginLabel.text = highlightSelectedTextRange(
            loginLabel.text,
            loginLabel.text.length - HIGHLIGHTED_LOGIN_SPANS_COUNT,
            loginLabel.text.length,
            HIGHLIGHTED_COLOR
        )
    }

    private fun assignValidation() {
        listOf(
            registerBinding.etName,
            registerBinding.etLogin,
            registerBinding.etPassword
        ).forEach {
            it.addTextChangedListener(
                FieldsValidation(
                    it,
                    it.parent.parent as TextInputLayout,
                    viewModel,
                    registerBinding.termsCheckbox
                )
            )
        }
    }

    override fun onDestroy() {
        _registerBinding = null
        super.onDestroy()
    }
}

