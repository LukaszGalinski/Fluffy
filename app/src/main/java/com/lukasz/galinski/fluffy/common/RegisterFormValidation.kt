package com.lukasz.galinski.fluffy.common

import android.text.Editable
import android.text.TextWatcher
import android.widget.CheckBox
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.viewmodel.LoginViewModel

private const val NAME = "NAME"
private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"
private const val NAME_MAX_LENGTH = 14

private val correctFields = mutableMapOf(
    NAME to false,
    EMAIL to false,
    PASSWORD to false,
)

class FieldsValidation(
    private val et: TextInputEditText,
    private val textInputLayout: TextInputLayout,
    private val viewModel: LoginViewModel,
    private val termsCheckbox: CheckBox
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when (et.id) {
            R.id.et_name -> {
                if (s!!.length < NAME_MAX_LENGTH) {
                    setInputPositive(NAME)
                } else {
                    setInputNegative(NAME)
                }
                if (s.isEmpty()) {
                    setInputEmptyError(NAME)
                }
            }

            R.id.et_login -> {
                if (FormValidationRules.validationEmail(s)) {
                    setInputPositive(EMAIL)
                } else {
                    setInputNegative(EMAIL)
                }
                if (s!!.isEmpty()) {
                    setInputEmptyError(EMAIL)
                }
            }
            R.id.et_password -> {
                if (FormValidationRules.passwordValidation(s.toString())) {
                    setInputPositive(PASSWORD)
                } else {
                    setInputNegative(PASSWORD)
                }
                if (s!!.isEmpty()) {
                    setInputEmptyError(PASSWORD)
                }
            }
        }
    }

    private fun setInputPositive(inputLabel: String) {
        correctFields[inputLabel] = true
        textInputLayout.error = null
    }

    private fun setInputNegative(inputLabel: String) {
        textInputLayout.error = et.context.getString(R.string.email_occupied)
        correctFields[inputLabel] = false
    }

    private fun setInputEmptyError(inputLabel: String) {
        textInputLayout.error = et.context.getString(R.string.register_error_empty)
        correctFields[inputLabel] = false
    }

    override fun afterTextChanged(s: Editable?) {
        searchEmpty()
    }

    private fun searchEmpty() {
        var areAllFieldsValid = true
        correctFields.forEach { (_, value) ->
            if (!value || !termsCheckbox.isChecked) {
                areAllFieldsValid = false
                return@forEach
            }
        }
        viewModel.setSaveButtonState(areAllFieldsValid)
    }
}


