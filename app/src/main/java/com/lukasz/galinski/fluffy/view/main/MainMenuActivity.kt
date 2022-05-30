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
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val BACK_BUTTON_DELAY = 1000L

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainMenuActivity::class.java)
        }
    }

    private val mainViewModel: MainMenuViewModel by viewModels()
    private var doubleCheckButton = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_host_layout)
        Runnable { createBackButtonDelay() }
    }

    private fun createBackButtonDelay() {
        if (doubleCheckButton) {
            super.onBackPressed()
            return
        }
        doubleCheckButton = true
        this.createToast(resources.getString(R.string.double_press_to_exit))
        handler.postDelayed(runnable, BACK_BUTTON_DELAY)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }
}