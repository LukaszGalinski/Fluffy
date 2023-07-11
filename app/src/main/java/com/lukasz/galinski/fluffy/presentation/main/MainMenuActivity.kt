package com.lukasz.galinski.fluffy.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.lukasz.galinski.fluffy.R
import com.lukasz.galinski.fluffy.databinding.MainHostLayoutBinding
import com.lukasz.galinski.fluffy.presentation.NotificationBuilder
import com.lukasz.galinski.fluffy.presentation.account.AccountHostActivity
import com.lukasz.galinski.fluffy.presentation.common.LoginEntryPoint
import com.lukasz.galinski.fluffy.presentation.common.logDebug
import com.lukasz.galinski.fluffy.presentation.common.logInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainMenuActivity : AppCompatActivity() {

    private lateinit var notification: NotificationBuilder

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, MainMenuActivity::class.java)
        }
    }

    private var _mainMenuHostBinding: MainHostLayoutBinding? = null
    private val hostViewModel: MainMenuViewModel by viewModels()
    private val mainMenuHostBinding get() = _mainMenuHostBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _mainMenuHostBinding = MainHostLayoutBinding.inflate(layoutInflater)
        setContentView(mainMenuHostBinding.root)

        notification = NotificationBuilder(this)
        setupTopBar()
        createMonthSpinner()
        createDrawerMenu()
    }

    private fun createDrawerMenu() {
        val drawerLayout = mainMenuHostBinding.mainMenuDrawerRoot

        mainMenuHostBinding.materialTopBar.setNavigationOnClickListener {
            drawerLayout.open()
        }

        mainMenuHostBinding.drawerNavigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.notification_simulation_single -> {
                    logDebug("Simulate single notification")
                    notification
                        .createChanel()
                        .setNotificationTypeId(1)
                        .setNotificationContentTitle(getString(R.string.subscription_notification_title, "Netflix"))
                        .build()
                }

                R.id.notification_simulation_10 -> {
                    repeat(10) { times ->
                        notification
                            .createChanel()
                            .setNotificationTypeId(times + 1)
                            .setNotificationContentTitle(getString(R.string.subscription_notification_title, "Amazon"))
                            .build()
                    }
                    logDebug("Simulate 10 notifications")
                }
            }
            drawerLayout.close()
            true
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
            when (menuItem.itemId) {
                R.id.notifications -> Unit
                R.id.logout -> {
                    hostViewModel.logoutUser()
                    finishAfterTransition()
                    startActivity(AccountHostActivity.createIntent(this, LoginEntryPoint.LOGIN))
                }
            }
            logInfo(menuItem.itemId.toString())
            true
        }
    }

    private fun createMonthSpinner() {
        mainMenuHostBinding.spinnerIcon.setOnClickListener {
            mainMenuHostBinding.monthSpinner.performClick()
        }

        val spinnerAdapter =
            ArrayAdapter(
                this,
                R.layout.support_simple_spinner_dropdown_item,
                resources.getStringArray(R.array.months_of_year)
            )

        mainMenuHostBinding.monthSpinner.apply {
            adapter = spinnerAdapter
            setSelection(hostViewModel.getCurrentMonth())

            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    logInfo("positionSelected: ${(position + 1)}")
                }

                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        }
    }

    override fun onDestroy() {
        _mainMenuHostBinding = null
        super.onDestroy()
    }
}