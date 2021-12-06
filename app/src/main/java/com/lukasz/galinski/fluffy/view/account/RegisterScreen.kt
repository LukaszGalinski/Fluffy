package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.fluffy.common.FieldsValidation
import com.lukasz.galinski.fluffy.common.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.databinding.RegisterScreenFragmentBinding

private const val HIGHLIGHTED_TERMS_SPANS_COUNT = 35
private const val HIGHLIGHTED_LOGIN_SPANS_COUNT = 5
private const val HIGHLIGHTED_COLOR = "#7F3DFF"

class RegisterScreen : Fragment() {

    private var _registerBinding: RegisterScreenFragmentBinding? = null
    private val registerBinding get() = _registerBinding!!

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
    }

    private fun highlightTextParts(){
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
                    it.parent.parent as TextInputLayout
                )
            )
        }
    }
}