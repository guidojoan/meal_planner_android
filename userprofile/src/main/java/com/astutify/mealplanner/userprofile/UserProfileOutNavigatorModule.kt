package com.astutify.mealplanner.userprofile

import androidx.appcompat.app.AppCompatActivity
import dagger.Module
import dagger.Provides

@Module
abstract class UserProfileOutNavigatorModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun userProfileNavigationFactoryProvider(
            activity: AppCompatActivity,
            factory: UserProfileOutNavigator.Factory
        ): UserProfileOutNavigator {
            return factory.create(activity)
        }
    }
}
