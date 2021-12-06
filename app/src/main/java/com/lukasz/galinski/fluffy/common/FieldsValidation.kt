package com.lukasz.galinski.fluffy.common

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.lukasz.galinski.fluffy.R

private const val NAME = "NAME"
private const val LAST_NAME = "LAST_NAME"
private const val EMAIL = "EMAIL"
private const val PASSWORD = "PASSWORD"
private const val NAME_MAX_LENGTH = 14

private val correctFields = mutableMapOf(
    NAME to false,
    LAST_NAME to false,
    EMAIL to false,
    PASSWORD to false
)

class FieldsValidation(
    private val et: TextInputEditText,
    private val textInputLayout: TextInputLayout,
    //private val viewModel: LoginViewModel
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when (et.id) {
            R.id.et_name -> {
                if (s!!.length < NAME_MAX_LENGTH) {
                    correctFields[NAME] = true
                    textInputLayout.error = null
                } else {
                    textInputLayout.error = et.context.getString(R.string.register_error)
                    correctFields[NAME] = false
                }
                if (s.isEmpty()) {
                    textInputLayout.error = et.context.getString(R.string.register_error_empty)
                    correctFields[NAME] = false
                }
            }

            R.id.et_login -> {
                if (FormValidator.validationEmail(s)) {
                    correctFields[EMAIL] = true
                    textInputLayout.error = null
                } else {
                    textInputLayout.error = et.context.getString(R.string.register_error)
                    correctFields[EMAIL] = false
                }
                if (s!!.isEmpty()) {
                    textInputLayout.error = et.context.getString(R.string.register_error_empty)
                    correctFields[EMAIL] = false
                }
            }
            R.id.et_password -> {
                if (FormValidator.passwordValidation(s.toString())) {
                    correctFields[PASSWORD] = true
                    textInputLayout.error = null
                } else {
                    textInputLayout.error = et.context.getString(R.string.register_error)
                    correctFields[PASSWORD] = false
                }
                if (s!!.isEmpty()) {
                    textInputLayout.error = et.context.getString(R.string.register_error_empty)
                    correctFields[PASSWORD] = false
                }
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
        searchEmpty()
    }

    private fun searchEmpty() {
        var areAllFieldsValid = true
        correctFields.forEach { (_, value) ->
            if (!value) {
                areAllFieldsValid = false
                return@forEach
            }
        }
       // viewModel.setSaveButtonState(areAllFieldsValid)
    }
}


