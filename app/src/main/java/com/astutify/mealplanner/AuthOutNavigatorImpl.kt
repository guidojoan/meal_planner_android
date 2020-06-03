package com.astutify.mealplanner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.auth.AuthOutNavigator
import com.astutify.mealplanner.userprofile.presentation.house.HouseEditActivity
import javax.inject.Inject

class AuthOutNavigatorImpl constructor(
    private val activity: AppCompatActivity
) : AuthOutNavigator {

    override fun goToHome() {
        activity.startActivity(Intent(activity, MainActivity::class.java))
        activity.finish()
    }

    override fun goToHouseEdit() {
        activity.startActivity(Intent(activity, HouseEditActivity::class.java))
        activity.finish()
    }

    class Factory @Inject constructor() : AuthOutNavigator.Factory {
        override fun create(activity: AppCompatActivity): AuthOutNavigator {
            return AuthOutNavigatorImpl(activity)
        }
    }
}
