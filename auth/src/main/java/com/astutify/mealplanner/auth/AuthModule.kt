package com.astutify.mealplanner.auth

import com.astutify.mealplanner.auth.data.LoginApiRepository
import com.astutify.mealplanner.auth.data.LoginDataRepository
import com.astutify.mealplanner.auth.domain.LoginRepository
import dagger.Module
import dagger.Provides

@Module
class AuthModule {

    @Provides
    fun providesLoginRepository(api: LoginApiRepository): LoginRepository {
        return LoginDataRepository(api)
    }
}
