package com.astutify.mealplanner.core.extension

import android.text.Editable

fun Editable.getAsInt(): Int {
    return try {
        this.toString().toInt()
    } catch (error: Throwable) {
        0
    }
}

fun Editable.getAsFloat(): Float {
    return try {
        this.toString().toFloat()
    } catch (error: Throwable) {
        0f
    }
}

fun Editable.getAsIntNullable(): Int? {
    return try {
        this.toString().toInt()
    } catch (error: Throwable) {
        null
    }
}

fun Editable.getAsFloatNullable(): Float? {
    return try {
        this.toString().toFloat()
    } catch (error: Throwable) {
        null
    }
}
