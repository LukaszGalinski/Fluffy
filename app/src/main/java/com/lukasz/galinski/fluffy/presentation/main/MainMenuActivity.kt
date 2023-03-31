package com.lukasz.galinski.fluffy.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.presentation.createToast
import com.lukasz.galinski.fluffy.databinding.MainHostLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    companion object {
        private const val BACK_BUTTON_DELAY = 1000L

        fun createIntent(context: Context): Intent {
            return Intent(context, MainMenuActivity::class.java)
        }
    }

    private var _mainMenuHostBinding: MainHostLayoutBinding? = null
    private val mainMenuHostBinding get() = _mainMenuHostBinding!!
    private var doubleCheckButton = false
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainMenuHostBinding = MainHostLayoutBinding.inflate(layoutInflater)
        setContentView(mainMenuHostBinding.root)

        runnable = Runnable { doubleCheckButton = false }
    }

    override fun onBackPressed() =
        when (findNavController(R.id.fragmentContainerView).graph.startDestinationId) {
            findNavController(R.id.fragmentContainerView).currentDestination?.id -> {
                createBackButtonDelay()
            }
            else -> onBackPressedDispatcher.onBackPressed()
        }

    private fun createBackButtonDelay() {
        if (doubleCheckButton) {
            finishAndRemoveTask()
            finishAffinity()
            return
        }
        doubleCheckButton = true
        this.createToast(resources.getString(R.string.double_press_to_exit))
        handler.postDelayed(runnable, BACK_BUTTON_DELAY)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        handler.removeCallbacksAndMessages(Unit)
        _mainMenuHostBinding = null
        super.onDestroy()
    }
}