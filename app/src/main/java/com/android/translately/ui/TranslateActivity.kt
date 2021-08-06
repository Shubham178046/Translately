package com.android.translately.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.translately.R
import com.android.translately.databinding.ActivityTranslateBinding

class TranslateActivity : AppCompatActivity() {
    val _binding: ActivityTranslateBinding by lazy {
        ActivityTranslateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)

    }
}