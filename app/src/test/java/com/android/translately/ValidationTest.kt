package com.android.translately

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.android.translately.util.LanguageUtil
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class ValidationTest {
    lateinit var languageUtil: LanguageUtil
    lateinit var instrumentationContext: Context

    var context: Context = mock(Context::class.java)
    @Before
    fun setUp() {
        languageUtil = LanguageUtil()
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun checkValidation() {
         //context = ApplicationProvider.getApplicationContext<Context>()
        val result = LanguageUtil.Companion.getLanguageName("en", context)
        //val result = LanguageUtil.Companion.a()
        assertThat(result).isNotEmpty()
    }
}