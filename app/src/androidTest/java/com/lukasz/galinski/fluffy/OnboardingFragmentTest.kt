package com.lukasz.galinski.fluffy

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withTagValue
import androidx.test.espresso.matcher.ViewMatchers.withText
import com.lukasz.galinski.core.data.OnboardingDetailsModel
import com.lukasz.galinski.fluffy.presentation.onboarding.OnboardingFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class OnboardingFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun prepareView() {
        hiltRule.inject()

        launchFragmentInHiltContainer<OnboardingFragment> {
            OnboardingFragment(
                OnboardingDetailsModel(
                    title = "My test title",
                    subtitle = "Example subtitle",
                    logoImage = R.drawable.icon_shop
                )
            )
        }
    }

    @Test
    fun checkReceivedImageDisplayedOnScreen() {
        onView(withId(R.id.onboarding_logo)).check(matches(withTagValue(equalTo(R.drawable.icon_shop))))
    }

    @Test
    fun checkReceivedTitleDisplayedOnScreen() {
        onView(withId(R.id.onboarding_title)).check(matches(withText("My test title")))
    }

    @Test
    fun checkReceivedSubtitleDisplayedOnScreen() {
        onView(withId(R.id.onboarding_subTitle)).check(matches(withText("Example subtitle")))
    }
}
