package com.astutify.mealplanner.userprofile

import androidx.appcompat.app.AppCompatActivity

interface UserProfileOutNavigator {

    fun goToHome()

    interface Factory {
        fun create(activity: AppCompatActivity): UserProfileOutNavigator
    }
}
