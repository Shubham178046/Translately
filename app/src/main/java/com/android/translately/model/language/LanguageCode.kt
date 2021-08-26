package com.android.translately.model.language

enum class LanguageCode(val value: Int) {
    SOURCE_CODE(1),
    DESTINATION_CODE(2);
    companion object {
        fun fromInt(value: Int) = LanguageCode.values().first { it.value == value }
    }
}