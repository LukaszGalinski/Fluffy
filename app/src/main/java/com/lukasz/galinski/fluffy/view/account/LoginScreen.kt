package com.lukasz.galinski.fluffy.view.account

import android.graphics.Color
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
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding


private const val MARKED_SPANS_COUNT = 7

class LoginScreen : Fragment() {

    private var _loginBinding: LoginScreenFragmentBinding? = null
    private val loginBinding get() = _loginBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _loginBinding = LoginScreenFragmentBinding.inflate(inflater)
        return loginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        markSignUpWord()
    }

    private fun markSignUpWord() {
        val textInfo = loginBinding.createAccountInfo
        val spannable = SpannableString(textInfo.text)
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(activity?.applicationContext!!, R.color.purple_primary)),
            textInfo.text.length - MARKED_SPANS_COUNT, // start
            textInfo.text.length, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        textInfo.text = spannable
    }
}