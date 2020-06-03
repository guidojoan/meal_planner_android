package com.astutify.mealplanner.auth

import com.astutify.mealplanner.auth.domain.LoginRepository
import com.astutify.mealplanner.auth.presentation.login.LoginActivity
import com.astutify.mealplanner.core.CoreComponent
import com.astutify.mealplanner.core.FeatureScope
import dagger.Component

@FeatureScope
@Component(
    modules = [AuthModule::class],
    dependencies = [CoreComponent::class, AuthOutNavigatorComponent::class]
)
interface AuthComponent {

    @Component.Builder
    interface Builder {

        fun withCoreComponent(component: CoreComponent): Builder

        fun withNavigatorComponent(navigator: AuthOutNavigatorComponent): Builder

        fun build(): AuthComponent
    }

    fun providesLoginRepository(): LoginRepository

    fun loginActivityBuilder(): LoginActivity.Component.Builder
}
