package com.astutify.mealplanner.core.utils

import android.os.Looper
import android.util.Log

class ThreadChecker {

    companion object {
        fun check(tag: String) {
            if (Looper.getMainLooper() != Looper.myLooper()) {
                Log.d("MP_LOG: ", "$tag -> on background")
            } else {
                Log.d("MP_LOG: ", "$tag -> on main")
            }
        }
    }
}
