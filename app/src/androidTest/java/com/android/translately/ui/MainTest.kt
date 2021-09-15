package com.android.translately.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
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
class MainTest : TestCase() {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun test_isActivityInView() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.splashMain)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isImageDisplayed() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.imageView3)).check(matches(isDisplayed()))
    }

    @Test
    fun test_isImageVisible() {
        val activityScenario = ActivityScenario.launch(SplashActivity::class.java)
        onView(withId(R.id.imageView3)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }
}