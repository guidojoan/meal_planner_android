package com.astutify.mealplanner.ingredient.presentation

import android.app.Application
import android.app.Instrumentation
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import com.astutify.mealplanner.MockApp

class MockAndroidJUnitRunner : AndroidJUnitRunner() {

    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return Instrumentation.newApplication(MockApp::class.java, context)
    }
}
