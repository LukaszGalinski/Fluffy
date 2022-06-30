package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.*
import com.lukasz.galinski.fluffy.databinding.RegisterScreenFragmentBinding
import com.lukasz.galinski.fluffy.model.UserModel
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val HIGHLIGHTED_TERMS_SPANS_COUNT = 35
private const val HIGHLIGHTED_LOGIN_SPANS_COUNT = 5
private const val HIGHLIGHTED_COLOR = "#7F3DFF"
private const val STATE_TAG = "State: "

@AndroidEntryPoint
class RegisterScreen : Fragment() {

    private var _registerBinding: RegisterScreenFragmentBinding? = null
    private val registerBinding get() = _registerBinding!!
    private val viewModel by viewModels<LoginViewModel>()

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
        viewModel.saveButtonState.observe(viewLifecycleOwner) {
            registerBinding.registerButton.markAs(it)
            registerBinding.registerButton.setStateAppearance()
        }

        registerBinding.termsCheckbox.setOnClickListener {
            registerBinding.etName.text = registerBinding.etName.text
        }

        registerBinding.existingAccountInfo.setOnClickListener {
            findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
        }

        registerBinding.registerButton.setOnClickListener {
            val userEmail = registerBinding.etLogin.text.toString()
            val userPassword = registerBinding.etPassword.text.toString()
            val userName = registerBinding.etName.text.toString()
            viewModel.saveUserIntoDatabase(UserModel(userName, userEmail, userPassword, "9999"))
        }
    }

    private fun handleRegisterStates() = lifecycleScope.launchWhenStarted {
        viewModel.userAccountState.collect { state ->
            when (state) {
                is Success -> {
                    Log.i(STATE_TAG, state.toString())
                    context?.createToast(resources.getString(R.string.user_created))
                    findNavController().navigate(R.id.action_registerScreen_to_loginScreen)
                }
                is Failure -> {
                    Log.i(STATE_TAG, state.toString())
                    context?.createToast(resources.getString(R.string.email_occupied))
                }
                is Loading -> {
                    Log.i(STATE_TAG, state.toString())
                    registerBinding.registerProgressBar.setVisible()
                }
                is Idle -> {
                    Log.i(STATE_TAG, state.toString())
                    registerBinding.registerProgressBar.setInvisible()
                }
                else -> {Log.i(STATE_TAG, state.toString()) }
            }
        }
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

