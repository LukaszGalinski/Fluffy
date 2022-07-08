package com.lukasz.galinski.fluffy.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.common.createToast
import com.lukasz.galinski.fluffy.databinding.MainHostLayoutBinding
import com.lukasz.galinski.fluffy.view.account.LoginHostActivity
import com.lukasz.galinski.fluffy.viewmodel.MainMenuViewModel
import dagger.hilt.android.AndroidEntryPoint


private const val BACK_BUTTON_DELAY = 1000L
private const val MAIN_MENU_ACTIVITY_TAG = "MainMenuActivity: "


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
        val currentMonth = mainViewModel.getCurrentMonth()
        val userId = intent.extras?.getLong(USER_ID_TAG)
        mainViewModel.userID = userId ?: 0
        createMonthSpinner(currentMonth)
        setupTopBar()
    }


    private fun setupTopBar() {
        mainMenuHostBinding.materialTopBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.notifications -> {
                    Log.i(MAIN_MENU_ACTIVITY_TAG, "Notifications")
                    true
                }
                R.id.logout -> {
                    Log.i(MAIN_MENU_ACTIVITY_TAG, "Logout")
                    mainViewModel.logoutUser()
                    LoginHostActivity.createIntent(applicationContext, "")
                    true
                }
                else -> false
            }
        }
    }

    private fun createMonthSpinner(currentMonth: Int) {
        val monthArray = resources.getStringArray(R.array.months_of_year)

        val spinnerAdapter: ArrayAdapter<String?> = object :
            ArrayAdapter<String?>(applicationContext, R.layout.spinner_adapter_view, monthArray) {
        }

        val dropdown = mainMenuHostBinding.monthSpinner
        dropdown.adapter = spinnerAdapter
        dropdown.setSelection(currentMonth)

        dropdown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.i(MAIN_MENU_ACTIVITY_TAG, (position + 1).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }
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