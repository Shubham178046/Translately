package com.android.translately

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.translately.util.LanguageUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ValidateString {
    lateinit var context: Context

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        //context=
    }

    @Test
    fun checkString() {
        val result = LanguageUtil.Companion.getLanguageName("en", context)
        assertThat(result.isNotEmpty()).isTrue()
    }
}