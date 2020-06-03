package com.astutify.mealplanner

import com.astutify.mealplanner.auth.AuthOutNavigatorComponent
import com.astutify.mealplanner.core.NavigationScope
import com.astutify.mealplanner.recipe.RecipeOutNavigatorComponent
import com.astutify.mealplanner.userprofile.UserProfileOutNavigatorComponent
import dagger.Component

@NavigationScope
@Component(modules = [NavigationModule::class])
interface NavigationComponent :
    AuthOutNavigatorComponent,
    RecipeOutNavigatorComponent,
    UserProfileOutNavigatorComponent {
    @Component.Builder
    interface Builder {

        fun build(): NavigationComponent
    }
}
