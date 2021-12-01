package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.RegisterScreenFragmentBinding

private const val HIGHLIGHTED_TERMS_SPANS_COUNT = 35
private const val HIGHLIGHTED_LOGIN_SPANS_COUNT = 5

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
        val checkBoxText = registerBinding.termsCheckbox
        val loginLabel = registerBinding.existingAccountInfo
        checkBoxText.text = highlightSelectedTextRange(
            checkBoxText.text,
            checkBoxText.text.length - HIGHLIGHTED_TERMS_SPANS_COUNT,
            checkBoxText.text.length
        )
        loginLabel.text = highlightSelectedTextRange(
            loginLabel.text,
            loginLabel.text.length - HIGHLIGHTED_LOGIN_SPANS_COUNT,
            loginLabel.text.length
        )
    }

    private fun highlightSelectedTextRange(
        message: CharSequence,
        startPosition: Int,
        endPosition: Int
    ): SpannableString {
        val spannable = SpannableString(message)
        spannable.setSpan(
            ForegroundColorSpan(
                ContextCompat.getColor(
                    activity?.applicationContext!!,
                    R.color.purple_primary
                )
            ),
            startPosition, // start
            endPosition, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }
}