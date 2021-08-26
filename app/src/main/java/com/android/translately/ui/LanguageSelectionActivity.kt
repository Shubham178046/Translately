package com.android.translately.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.translately.R
import com.android.translately.adapter.LanguageAdapter
import com.android.translately.databinding.ActivityLanguageSelectionBinding
import com.android.translately.model.language.Language
import com.android.translately.util.LanguageUtil


class LanguageSelectionActivity : AppCompatActivity() , View.OnTouchListener {
    lateinit var adapters: LanguageAdapter
    var code: Int? = -1
    var languages: ArrayList<Language>? = ArrayList<Language>()

    private var previousFingerPosition = 0
    private var baseLayoutPosition : Float = 0f
    private var defaultViewHeight = 0

    private var isClosing = false
    private var isScrollingUp = false
    private var isScrollingDown = false
    companion object {
        fun launch(context: AppCompatActivity, code: Int) {
            val intent = Intent(context, LanguageSelectionActivity::class.java)
            intent.putExtra("code", code)
            context.startActivityForResult(intent, code)
            context.overridePendingTransition(R.anim.slide_up, R.anim.stay);
        }
    }

    val binding: ActivityLanguageSelectionBinding by lazy {
        ActivityLanguageSelectionBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        actionListner()
    }

    private fun actionListner() {
        binding.edtSearchLanguage.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().trim().length != 0) {
                    performSearch(s.toString().trim())
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s.isNullOrEmpty()) {
                    languages = LanguageUtil.getData(this@LanguageSelectionActivity)
                    initializeRecyclerview(languages)
                }
            }

        })

        binding.imgClose.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.stay, R.anim.slide_down)
        }
    }

    private fun initViews() {
        code = intent.getIntExtra("code", -1)
        languages = LanguageUtil.getData(this)
        binding.baseLayout.setOnTouchListener(this)
        initializeRecyclerview(languages)
    }

    fun initializeRecyclerview(languages: ArrayList<Language>?) {
        adapters =
            LanguageAdapter(this, languages, object : LanguageAdapter.onClick {
                override fun onClick(language: Language) {
                    var bundle = Intent()
                    bundle.putExtra("language_code", language.languageCode)
                    setResult(code!!, bundle)
                    finish()
                    overridePendingTransition(R.anim.stay, R.anim.slide_down)
                }
            })
        binding.rvLanguageSelection.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LanguageSelectionActivity)
            adapter = adapters
        }
    }

    private fun performSearch(searchData: String) {
        var tempList: ArrayList<Language> =
            ArrayList<Language>()
        if (languages != null && languages!!.size > 0) {
            for (i in languages!!) {
                if (i.languageName!!.lowercase().contains(searchData.lowercase())) {
                    tempList.add(i)
                }
            }
            adapters.filterList(tempList)
        } else {
        }
    }

    fun closeDownAndDismissActivity(currentPosition : Float){
        isClosing = true
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val screenHeight: Float = size.y.toFloat()
        val positionAnimator: ObjectAnimator = ObjectAnimator.ofFloat(
            binding.baseLayout,
            "y",
            currentPosition,
            screenHeight + binding.baseLayout.getHeight()
        )
        positionAnimator.duration = 300
        positionAnimator.addListener(object : Animator.AnimatorListener{
            override fun onAnimationStart(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                finish()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationRepeat(p0: Animator?) {

            }
        })
        positionAnimator.start()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    override fun onTouch(view: View?, event: MotionEvent?): Boolean {
        val Y = event!!.getRawY().toInt()
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                // save default base layout height
                defaultViewHeight =  binding.baseLayout.getHeight()

                // Init finger and view position
                previousFingerPosition = Y
                baseLayoutPosition =  binding.baseLayout.getY()
            }
            MotionEvent.ACTION_UP -> {
                // If user was doing a scroll up
                if (isScrollingUp) {
                    // Reset baselayout position
                     binding.baseLayout.setY(0F)
                    // We are not in scrolling up mode anymore
                    isScrollingUp = false
                }

                // If user was doing a scroll down
                if (isScrollingDown) {
                    // Reset baselayout position
                     binding.baseLayout.setY(0F)
                    // Reset base layout size
                     binding.baseLayout.getLayoutParams().height = defaultViewHeight
                     binding.baseLayout.requestLayout()
                    // We are not in scrolling down mode anymore
                    isScrollingDown = false
                }
            }
            MotionEvent.ACTION_MOVE -> if (!isClosing) {
                val currentYPosition =  binding.baseLayout.getY()

                // If we scroll up
                if (previousFingerPosition > Y) {
                    // First time android rise an event for "up" move
                    if (!isScrollingUp) {
                        isScrollingUp = true
                    }

                    // Has user scroll down before -> view is smaller than it's default size -> resize it instead of change it position
                    if ( binding.baseLayout.getHeight() < defaultViewHeight) {
                         binding.baseLayout.getLayoutParams().height =
                             binding.baseLayout.getHeight() - (Y - previousFingerPosition)
                         binding.baseLayout.requestLayout()
                    } else {
                        // Has user scroll enough to "auto close" popup ?
                        if (baseLayoutPosition - currentYPosition > defaultViewHeight / 4) {
                         //   closeUpAndDismissDialog(currentYPosition)
                            return true
                        }

                        //
                    }
                     binding.baseLayout.setY( binding.baseLayout.getY() + (Y - previousFingerPosition))
                } else {

                    // First time android rise an event for "down" move
                    if (!isScrollingDown) {
                        isScrollingDown = true
                    }

                    // Has user scroll enough to "auto close" popup ?
                    if (Math.abs(baseLayoutPosition - currentYPosition) > defaultViewHeight / 2) {
                        closeDownAndDismissActivity(currentYPosition)
                        return true
                    }

                    // Change base layout size and position (must change position because view anchor is top left corner)
                     binding.baseLayout.setY( binding.baseLayout.getY() + (Y - previousFingerPosition))
                     binding.baseLayout.getLayoutParams().height =
                         binding.baseLayout.getHeight() - (Y - previousFingerPosition)
                     binding.baseLayout.requestLayout()
                }

                // Update position
                previousFingerPosition = Y
            }
        }
        return true
    }
}