package com.android.translately.ui

import android.animation.ValueAnimator
import android.content.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.LANG_AVAILABLE
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.android.translately.databinding.ActivityTranslateBinding
import com.android.translately.model.language.LanguageCode
import com.android.translately.model.requestModel.LanguageReq
import com.android.translately.util.LanguageUtil
import com.android.translately.viewmodel.TranslateViewModel
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*


@AndroidEntryPoint
class TranslateActivity : AppCompatActivity() {
    var sourceLanguage: String? = "en"
    var destinationLanguage: String? = "hi"
    val SOURCE_CODE = 1
    val DESTINATION_CODE = 2
    val REQ_CODE_SPEECH_INPUT = 101
    val anim = ValueAnimator.ofFloat(1f, 1.5f)
    private val viewModel: TranslateViewModel by viewModels()
    var textToSpeechSource: TextToSpeech? = null
    var textToSpeechDestination: TextToSpeech? = null
    val _binding: ActivityTranslateBinding by lazy {
        ActivityTranslateBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(_binding.root)
        initViews()
        actionListner()
    }

    private fun initViews() {
        _binding.txtLanguageName.setText(
            LanguageUtil.getLanguageName(sourceLanguage!!, this)
        )
        _binding.txtSourceLanguage.setText(
            LanguageUtil.getLanguageName(sourceLanguage!!, this)
        )
        _binding.imglanguage.setImageResource(
            LanguageUtil.getLanguageImage(sourceLanguage!!, this)
        )

        _binding.txtTranslatedLanguageName.setText(
            LanguageUtil.getLanguageName(destinationLanguage!!, this)
        )
        _binding.txtDestinationLanguage.setText(
            LanguageUtil.getLanguageName(destinationLanguage!!, this)
        )
        _binding.imgTranslatedlanguage.setImageResource(
            LanguageUtil.getLanguageImage(destinationLanguage!!, this)
        )

        textToSpeechSource = TextToSpeech(this, object : TextToSpeech.OnInitListener {
            override fun onInit(i: Int) {
                if (i != TextToSpeech.ERROR) {
                    if (textToSpeechSource!!.isLanguageAvailable(
                            Locale(
                                sourceLanguage
                            )
                        ) != LANG_AVAILABLE
                    ) {
                        _binding.imgSourceMicrophone.visibility = View.GONE
                        return
                    }
                    textToSpeechSource!!.setLanguage(Locale(sourceLanguage))
                } else {
                    _binding.imgSourceMicrophone.visibility = View.GONE
                }
            }
        })

        textToSpeechDestination = TextToSpeech(this, object : TextToSpeech.OnInitListener {
            override fun onInit(i: Int) {
                if (i != TextToSpeech.ERROR) {
                    if (textToSpeechDestination!!.isLanguageAvailable(
                            Locale(
                                destinationLanguage
                            )
                        ) != LANG_AVAILABLE
                    ) {
                        _binding.imgDestinationMicrophone.visibility = View.GONE
                        return
                    }
                    textToSpeechDestination!!.setLanguage(Locale(destinationLanguage))
                } else {
                    _binding.imgDestinationMicrophone.visibility = View.GONE
                }
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.conversion.collect { event ->
                when (event) {
                    is TranslateViewModel.TranslateEvent.Success -> {
                        _binding.txtTranslatedText.setText(event.convertText.toString())
                    }
                    is TranslateViewModel.TranslateEvent.Failure -> {
                        _binding.txtTranslatedText.setText("")
                    }
                    is TranslateViewModel.TranslateEvent.Loading -> {

                    }
                }
            }
        }
        anim.duration = 1000
        anim.addUpdateListener { animation ->
            _binding.imgCopy.setScaleX(animation.animatedValue as Float)
            _binding.imgCopy.setScaleY(animation.animatedValue as Float)
        }
        anim.repeatCount = 1
        anim.repeatMode = ValueAnimator.REVERSE
        println(
            "Default Language--->" + Locale(sourceLanguage).displayLanguage + " " + Locale(
                sourceLanguage
            ).language
        )
    }

    private fun actionListner() {
        _binding.llLanguage.setOnClickListener {
            LanguageSelectionActivity.launch(this, LanguageCode.SOURCE_CODE.value)
        }
        _binding.llTranslatedLanguage.setOnClickListener {
            LanguageSelectionActivity.launch(this, LanguageCode.DESTINATION_CODE.value)
        }

        _binding.edtTranslateText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0 != null && p0.isNotEmpty()) {
                    getTranslateLanguage(p0.toString(), destinationLanguage!!, sourceLanguage!!)
                    //FirebaseTranslation(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        _binding.imgSourceMicrophone.setOnClickListener {
            textToSpeechSource!!.speak(
                _binding.edtTranslateText.text.toString(),
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }

        _binding.imgDestinationMicrophone.setOnClickListener {
            textToSpeechDestination!!.speak(
                _binding.txtTranslatedText.text.toString(),
                TextToSpeech.QUEUE_FLUSH,
                null
            )
        }

        _binding.imgCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip: ClipData =
                ClipData.newPlainText("translated text", _binding.txtTranslatedText.text.toString())
            clipboard.setPrimaryClip(clip)
            anim.start()
            Toast.makeText(this, "Translate Copied", Toast.LENGTH_LONG).show()
        }

        _binding.imgMicrophone.setOnClickListener {
            startVoiceInput()
        }

        _binding.imgRefresh.setOnClickListener {
            val currentSourceLanguage = sourceLanguage
            val currentDestinationLanguage = destinationLanguage

            sourceLanguage = currentDestinationLanguage
            destinationLanguage = currentSourceLanguage

            _binding.txtLanguageName.setText(
                LanguageUtil.getLanguageName(sourceLanguage!!, this)
            )
            _binding.txtSourceLanguage.setText(
                LanguageUtil.getLanguageName(sourceLanguage!!, this)
            )
            _binding.imglanguage.setImageResource(
                LanguageUtil.getLanguageImage(sourceLanguage!!, this)
            )

            _binding.txtTranslatedLanguageName.setText(
                LanguageUtil.getLanguageName(destinationLanguage!!, this)
            )
            _binding.txtDestinationLanguage.setText(
                LanguageUtil.getLanguageName(destinationLanguage!!, this)
            )
            _binding.imgTranslatedlanguage.setImageResource(
                LanguageUtil.getLanguageImage(destinationLanguage!!, this)
            )
        }
    }

    fun FirebaseTranslation(translatedString: String) {
        val options = FirebaseTranslatorOptions.Builder()
            .setSourceLanguage(FirebaseTranslateLanguage.languageForLanguageCode(sourceLanguage!!)!!)
            .setTargetLanguage(FirebaseTranslateLanguage.languageForLanguageCode(destinationLanguage!!)!!)
            .build()

        val Translator = FirebaseNaturalLanguage.getInstance().getTranslator(options)
        val conditions = FirebaseModelDownloadConditions.Builder()
            .build()
        Translator
            .downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                Translator.translate(translatedString)
                    .addOnSuccessListener { translatedText ->
                        println("Translated Text----->" + translatedText)
                        _binding.txtTranslatedText.setText(translatedText)
                    }
                    .addOnFailureListener { exception ->
                        println("Translated Text Error----->" + exception.localizedMessage)
                        _binding.txtTranslatedText.setText("")
                    }
            }
            .addOnFailureListener { exception ->
                println("Exception----->" + exception.localizedMessage)
            }
    }

    fun getTranslateLanguage(query: String, target: String, source: String) {
        var languageReq: LanguageReq = LanguageReq()
        languageReq.q = query
        languageReq.source = source
        languageReq.target = target
        viewModel.convert(query, target, source)
    }

    private fun startVoiceInput() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        var language: ArrayList<String> = ArrayList<String>()
        language.add(sourceLanguage.toString())
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, sourceLanguage);
        intent.putExtra("android.speech.extra.EXTRA_ADDITIONAL_LANGUAGES", language)
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to translate")
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (a: ActivityNotFoundException) {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == resultCode) {
            when (resultCode) {
                SOURCE_CODE -> {
                    if (data != null) {
                        sourceLanguage = data.getStringExtra("language_code")
                        _binding.txtLanguageName.setText(
                            LanguageUtil.getLanguageName(sourceLanguage!!, this)
                        )
                        _binding.edtTranslateText.setText("")
                        _binding.txtSourceLanguage.setText(
                            LanguageUtil.getLanguageName(sourceLanguage!!, this)
                        )
                        _binding.imglanguage.setImageResource(
                            LanguageUtil.getLanguageImage(sourceLanguage!!, this)
                        )
                        textToSpeechSource =
                            TextToSpeech(this, object : TextToSpeech.OnInitListener {
                                override fun onInit(i: Int) {
                                    if (i != TextToSpeech.ERROR) {
                                        if (textToSpeechSource!!.isLanguageAvailable(
                                                Locale(
                                                    sourceLanguage
                                                )
                                            ) != LANG_AVAILABLE
                                        ) {
                                            _binding.imgSourceMicrophone.visibility = View.GONE
                                            return
                                        }
                                        textToSpeechSource!!.setLanguage(Locale(sourceLanguage))
                                    } else {
                                        _binding.imgSourceMicrophone.visibility = View.GONE
                                    }
                                }
                            })
                    }
                }
                DESTINATION_CODE -> {
                    if (data != null) {
                        destinationLanguage = data.getStringExtra("language_code")
                        _binding.txtTranslatedLanguageName.setText(
                            LanguageUtil.getLanguageName(destinationLanguage!!, this)
                        )
                        _binding.txtTranslatedText.setText("")
                        _binding.txtDestinationLanguage.setText(
                            LanguageUtil.getLanguageName(destinationLanguage!!, this)
                        )
                        _binding.imgTranslatedlanguage.setImageResource(
                            LanguageUtil.getLanguageImage(destinationLanguage!!, this)
                        )
                        textToSpeechDestination =
                            TextToSpeech(this, object : TextToSpeech.OnInitListener {
                                override fun onInit(i: Int) {
                                    if (i != TextToSpeech.ERROR) {
                                        if (textToSpeechDestination!!.isLanguageAvailable(
                                                Locale(
                                                    destinationLanguage
                                                )
                                            ) != LANG_AVAILABLE
                                        ) {
                                            _binding.imgDestinationMicrophone.visibility = View.GONE
                                            return
                                        }
                                        textToSpeechDestination!!.setLanguage(
                                            Locale(
                                                destinationLanguage
                                            )
                                        )
                                    } else {
                                        _binding.imgDestinationMicrophone.visibility = View.GONE
                                    }
                                }
                            })
                    }
                }
            }
        } else if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && null != data) {
                val result: ArrayList<String>? =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                _binding.edtTranslateText.setText(result!!.get(0))
            }
        }
    }
}