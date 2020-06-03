package com.astutify.mealplanner.auth.data

import com.astutify.mealplanner.auth.domain.LoginRepository
import com.astutify.mealplanner.core.entity.data.AuthData
import com.astutify.mealplanner.core.entity.data.GoogleUser
import io.reactivex.Single
import javax.inject.Inject

class LoginDataRepository @Inject constructor(
    private val apiRepository: LoginApiRepository
) : LoginRepository {

    override fun login(googleUser: GoogleUser): Single<AuthData> {
        return apiRepository.login(googleUser)
    }
}
