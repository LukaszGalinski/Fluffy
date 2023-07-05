package com.lukasz.galinski.fluffy.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.MainHostLayoutBinding
import com.lukasz.galinski.fluffy.presentation.account.AccountHostActivity
import com.lukasz.galinski.fluffy.presentation.common.LoginEntryPoint
import com.lukasz.galinski.fluffy.presentation.common.logInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainMenuActivity::class.java)
        }
    }

    private var _mainMenuHostBinding: MainHostLayoutBinding? = null
    private val hostViewModel: MainMenuViewModel by viewModels()
    private val mainMenuHostBinding get() = _mainMenuHostBinding!!

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainMenuHostBinding = MainHostLayoutBinding.inflate(layoutInflater)
        setContentView(mainMenuHostBinding.root)

        createMonthSpinner()
        createDrawerMenu()
        showSpinnerItemsOnImagePressed()
        setupTopBar()
    }

    private fun createDrawerMenu() {
        val drawerLayout = mainMenuHostBinding.mainMenuDrawerRoot
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            mainMenuHostBinding.mainMenuDrawerRoot,
            R.string.nav_open,
            R.string.nav_close
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        mainMenuHostBinding.materialTopBar.setNavigationOnClickListener {
            drawerLayout.open()
        }
    }

    fun hideAppBar() {
        mainMenuHostBinding.materialTopBar.visibility = View.GONE
    }

    fun showAppBar() {
        mainMenuHostBinding.materialTopBar.visibility = View.VISIBLE
    }

    private fun setupTopBar() {
        mainMenuHostBinding.materialTopBar.setOnMenuItemClickListener { menuItem ->
            logInfo(menuItem.itemId.toString())

            when (menuItem.itemId) {
                R.id.notifications -> {
                    true
                }

                R.id.logout -> {
                    hostViewModel.logoutUser()
                    finishAfterTransition()
                    startActivity(AccountHostActivity.createIntent(this, LoginEntryPoint.LOGIN))
                    true
                }

                else -> false
            }
        }
    }

    private fun showSpinnerItemsOnImagePressed() {
        mainMenuHostBinding.spinnerIcon.setOnClickListener {
            mainMenuHostBinding.monthSpinner.performClick()
        }
    }

    private fun createMonthSpinner() {
        val currentMonth = hostViewModel.getCurrentMonth()
        val monthArray = resources.getStringArray(R.array.months_of_year)

        val spinnerAdapter: ArrayAdapter<String?> = object :
            ArrayAdapter<String?>(this, R.layout.spinner_adapter_view, monthArray) {
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
                logInfo("positionSelected: ${(position + 1)}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                return
            }
        }
    }

    override fun onDestroy() {
        _mainMenuHostBinding = null
        super.onDestroy()
    }
}