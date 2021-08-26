package com.android.translately.util

import android.content.Context
import com.android.translately.model.language.Language
import java.util.*
import kotlin.collections.ArrayList

open class LanguageUtil {
    companion object {
        var languageCode = arrayOf(
            "af",
            "ar",
            "be",
            "bg",
            "bn",
            "ca",
            "da",
            "de",
            "el",
            "en",
            "eo",
            "es",
            "et",
            "fa",
            "fi",
            "fr",
            "ga",
            "gl",
            "gu",
            "hi",
            "hr",
            "ht",
            "hu",
            "id",
            "is",
            "it",
            "ja",
            "ka",
            "ko",
            "lt",
            "lv",
            "mk",
            "mr",
            "ms",
            "mt",
            "nl",
            "no",
            "pl",
            "pt",
            "ro",
            "ru",
            "sk",
            "sl",
            "sq",
            "sv",
            "sw",
            "ta",
            "te",
            "th",
            "tl",
            "tr",
            "uk",
            "ur",
            "vi",
            "zh",
        )

        open fun getData(context: Context): ArrayList<Language> {
            val arrayList: ArrayList<Language> = ArrayList()
            val strArr: Array<String> = languageCode
            for (str in strArr) {
                println("Name------------->" + str)
                val language = Language()
                language.languageCode = str
                language.languageName = context.getString(
                    context.getResources().getIdentifier(str, "string", context.getPackageName())
                )
                arrayList.add(language)
            }
            Collections.sort(arrayList, object : java.util.Comparator<Language> {
                override fun compare(p0: Language?, p1: Language?): Int {
                    return p0!!.languageName!!.compareTo(p1!!.languageName!!)
                }

            })
            return arrayList
        }

        fun getLanguageName(code: String, context: Context): String {
            return context.getString(
                context.getResources().getIdentifier(code, "string", context.getPackageName())
            )
        }

        fun getLanguageImage(code: String, context: Context) : Int
        {
           return context.resources.getIdentifier(
                "flag_" +
                        code,
                "drawable",
                context.packageName
            )
        }
        open fun a(): String {
            val lowerCase = Locale.getDefault().toString().toLowerCase()
            val language = Locale.getDefault().language
            val strArr: Array<String> = languageCode
            for (str in strArr) {
                if (str == lowerCase) {
                    return lowerCase
                }
                if (str == language) {
                    return language
                }
            }
            return "en"
        }
    }

}