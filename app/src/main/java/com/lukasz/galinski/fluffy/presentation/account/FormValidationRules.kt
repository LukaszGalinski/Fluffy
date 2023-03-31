package com.lukasz.galinski.fluffy.presentation.account

import java.util.regex.Pattern

object FormValidationRules {
    private const val PASSWORD_LENGTH = 6
    private const val SPECIAL_CHARACTER_REGEX = "[^[a-z0-9A-Z]]"
    private const val DIGIT_REGEX = "\\d+"
    private val digitPattern = Pattern.compile(DIGIT_REGEX)
    private val specialCharacterPattern = Pattern.compile(SPECIAL_CHARACTER_REGEX)

    fun validationEmail(s: CharSequence?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(s!!).matches()
    }

    fun passwordValidation(userPassword: String): Boolean {
        val digitMatcher = digitPattern.matcher(userPassword)
        val specialSignMatcher = specialCharacterPattern.matcher(userPassword)
        return userPassword.length >= PASSWORD_LENGTH && digitMatcher.find() && specialSignMatcher.find()
    }
}