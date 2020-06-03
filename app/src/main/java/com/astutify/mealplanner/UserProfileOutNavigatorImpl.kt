package com.astutify.mealplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import javax.inject.Inject

class UserProfileOutNavigatorImpl constructor(
    private val activity: AppCompatActivity
) : UserProfileOutNavigator {

    override fun goToHome() {
        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

    class Factory @Inject constructor() : UserProfileOutNavigator.Factory {
        override fun create(activity: AppCompatActivity): UserProfileOutNavigator {
            return UserProfileOutNavigatorImpl(activity)
        }
    }
}
