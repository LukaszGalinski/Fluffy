package com.lukasz.galinski.fluffy.view.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lukasz.galinski.fluffy.databinding.LoginScreenFragmentBinding

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
}