package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.highlightSelectedTextRange
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding

private const val MARKED_SPANS_COUNT = 7
private const val HIGHLIGHTED_COLOR = "#7F3DFF"

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
        val registerLabel = loginBinding.createAccountInfo
        registerLabel.text = highlightSelectedTextRange(
            registerLabel.text,
            registerLabel.text.length - MARKED_SPANS_COUNT,
            registerLabel.text.length,
            HIGHLIGHTED_COLOR
        )

        loginBinding.createAccountInfo.setOnClickListener {
            findNavController().navigate(R.id.action_loginScreen_to_registerScreen)
        }
    }
}