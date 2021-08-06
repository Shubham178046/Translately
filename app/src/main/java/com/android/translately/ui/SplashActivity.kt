package com.android.translately.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.translately.databinding.ActivitySplashBinding
import com.android.translately.model.Language
import com.android.translately.util.LanguageUtil
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.ArrayList

class SplashActivity : AppCompatActivity() {
    var arrayList: ArrayList<Language>? = null
    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val name = getString(resources.getIdentifier("af", "string", packageName))
        println(name)
        arrayList = LanguageUtil.getData(this)
    }
}