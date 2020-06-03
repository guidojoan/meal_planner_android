package com.astutify.mealplanner.userprofile

import com.astutify.mealplanner.core.CoreComponent
import com.astutify.mealplanner.core.FeatureScope
import com.astutify.mealplanner.userprofile.presentation.house.HouseEditActivity
import com.astutify.mealplanner.userprofile.presentation.profile.UserProfileFragment
import dagger.Component

@FeatureScope
@Component(
    modules = [UserProfileModule::class],
    dependencies = [CoreComponent::class, UserProfileOutNavigatorComponent::class]
)
interface UserProfileComponent {

    @Component.Builder
    interface Builder {

        fun withCoreComponent(component: CoreComponent): Builder

        fun withNavigatorComponent(navigator: UserProfileOutNavigatorComponent): Builder

        fun build(): UserProfileComponent
    }

    fun houseEditActivityBuilder(): HouseEditActivity.Component.Builder

    fun userProfileFragmentBuilder(): UserProfileFragment.Component.Builder
}
