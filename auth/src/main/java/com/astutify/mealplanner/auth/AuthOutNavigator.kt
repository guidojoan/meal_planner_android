package com.astutify.mealplanner.auth

import androidx.appcompat.app.AppCompatActivity

interface AuthOutNavigator {

    fun goToHome()

    fun goToHouseEdit()

    interface Factory {
        fun create(activity: AppCompatActivity): AuthOutNavigator
    }
}
