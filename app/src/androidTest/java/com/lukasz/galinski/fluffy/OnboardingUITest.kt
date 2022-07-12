package com.lukasz.galinski.fluffy

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.lukasz.galinski.fluffy.view.onboarding.OnBoardingScreenActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
class OnboardingUITest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var activityRule: ActivityScenarioRule<OnBoardingScreenActivity> =
        ActivityScenarioRule(OnBoardingScreenActivity::class.java)

    @Test
    fun test_name_matches_data_source() {
        hiltRule.inject()
        Espresso.onView(ViewMatchers.withId(R.id.onboarding_login_button))
            .check(
                ViewAssertions.matches(
                    Matchers.allOf(
                        ViewMatchers.isDisplayed(),
                        ViewMatchers.withText("Login")
                    )
                )
            )
    }
}