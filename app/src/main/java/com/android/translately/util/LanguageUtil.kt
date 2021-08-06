package com.android.translately.util

import android.content.Context
import com.android.translately.model.Language
import java.util.*
import kotlin.collections.ArrayList

open class LanguageUtil {
    companion object {
        var languageCode = arrayOf(
            "af",
            "ar_ae",
            "ar_eg",
            "ar_sa",
            "az",
            "be",
            "bg",
            "bn",
            "bs",
            "ca",
            "ceb",
            "cs",
            "cy",
            "da",
            "de",
            "el",
            "en_au",
            "en_uk",
            "en_us",
            "eo",
            "es_es",
            "es_mx",
            "es_us",
            "et",
            "eu",
            "fa",
            "fi",
            "fr_ca",
            "fr_fr",
            "ga",
            "gl",
            "gu",
            "ha",
            "hi",
            "hmn",
            "hr",
            "ht",
            "hu",
            "hy",
            "id",
            "ig",
            "is",
            "it",
            "iw",
            "ja",
            "jw",
            "ka",
            "kk",
            "km",
            "kn",
            "ko",
            "la",
            "lt",
            "lv",
            "mg",
            "mi",
            "mk",
            "ml",
            "mn",
            "mr",
            "ms",
            "mt",
            "my",
            "ne",
            "nl",
            "no",
            "ny",
            "pa",
            "pl",
            "pt_br",
            "pt_pt",
            "ro",
            "ru",
            "si",
            "sk",
            "sl",
            "so",
            "sq",
            "sr",
            "st",
            "su",
            "sv",
            "sw",
            "ta",
            "te",
            "tg",
            "th",
            "tl",
            "tr",
            "uk",
            "ur",
            "uz",
            "vi",
            "zh_cn",
            "zh_hk",
            "zh_tw",
            "zu"
        )

        open fun getData(context: Context): ArrayList<Language> {
            val arrayList: ArrayList<Language> = ArrayList()
            val strArr: Array<String> = languageCode
            for (str in strArr) {
                println("Name------------->"+str)
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