package com.android.translately.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.android.translately.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4ClassRunner::class)
class TranslateActivityTest : TestCase() {
    @get:Rule(order = 0)
    var hiltRules = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenario = ActivityScenarioRule(TranslateActivity::class.java)

    @Before
    fun setup() {
        hiltRules.inject()
    }

    @Test
    fun test_isActivityDisplayed() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isTestDisplayed() {
        onView(withId(R.id.appCompatTextView)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isTestVisible() {
        onView(withId(R.id.appCompatTextView)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun test_isTestString() {
        onView(withId(R.id.appCompatTextView)).check(matches(withText(R.string.translate)))
    }
}