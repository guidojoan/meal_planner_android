package com.astutify.mealplanner.core.utils

import java.util.Locale
import javax.inject.Inject

class LanguageUtils @Inject constructor() {

    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }
}
