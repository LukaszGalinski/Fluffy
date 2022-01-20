package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.FieldsValidation
import com.lukasz.galinski.fluffy.common.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.common.markAs
import com.lukasz.galinski.fluffy.common.setStateAppearance
import com.lukasz.galinski.fluffy.databinding.RegisterScreenFragmentBinding
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel

private const val HIGHLIGHTED_TERMS_SPANS_COUNT = 35
private const val HIGHLIGHTED_LOGIN_SPANS_COUNT = 5
private const val HIGHLIGHTED_COLOR = "#7F3DFF"

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
        viewModel.saveButtonState.observe(viewLifecycleOwner, {
            registerBinding.registerButton.markAs(it)
            registerBinding.registerButton.setStateAppearance()
        })

        registerBinding.termsCheckbox.setOnClickListener {
            registerBinding.etName.text = registerBinding.etName.text
        }

        registerBinding.existingAccountInfo.setOnClickListener {
            findNavController().navigate(R.id.action_registerScreen_pop)
        }

        registerBinding.registerButton.setOnClickListener {
            //save user into database
            //navigate to setup Pingit
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
}