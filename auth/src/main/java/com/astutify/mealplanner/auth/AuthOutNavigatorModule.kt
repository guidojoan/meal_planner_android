package com.astutify.mealplanner.auth

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
abstract class AuthOutNavigatorModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun filterNavigationFactoryProvider(
            activity: AppCompatActivity,
            factory: AuthOutNavigator.Factory
        ): AuthOutNavigator {
            return factory.create(activity)
        }
    }
}
