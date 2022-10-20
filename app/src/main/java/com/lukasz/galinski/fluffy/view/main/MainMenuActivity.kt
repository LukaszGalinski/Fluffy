package com.lukasz.galinski.fluffy.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.createToast
import com.lukasz.galinski.fluffy.databinding.MainHostLayoutBinding
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf


private const val BACK_BUTTON_DELAY = 1000L

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    companion object {
        private const val USER_ID_TAG = "UserId"
        fun createIntent(context: Context, userId: Long): Intent {
            val intent = Intent(context, MainMenuActivity::class.java)
            intent.putExtra(USER_ID_TAG, userId)
            return intent
        }
    }

    private var _mainMenuHostBinding: MainHostLayoutBinding? = null
    private val mainMenuHostBinding get() = _mainMenuHostBinding!!
    private val mainViewModel: MainMenuViewModel by viewModels()
    private var doubleCheckButton = false
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainMenuHostBinding = MainHostLayoutBinding.inflate(layoutInflater)
        setContentView(mainMenuHostBinding.root)
        val userId = intent.extras?.getLong(USER_ID_TAG)
        mainViewModel.userID = flowOf(userId ?: 0) as MutableStateFlow<Long>
    }

    override fun onBackPressed() {
        createBackButtonDelay()
    }

    private fun createBackButtonDelay() {
        if (doubleCheckButton) {
            super.onBackPressed()
            return
        }
        doubleCheckButton = true

        handler.postDelayed({
            this.createToast(resources.getString(R.string.double_press_to_exit))
            doubleCheckButton = false
        }, BACK_BUTTON_DELAY)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(Unit)
        _mainMenuHostBinding = null
        super.onDestroy()
    }
}