package com.astutify.mealplanner.auth.domain

import com.astutify.mealplanner.core.entity.data.AuthData
import com.astutify.mealplanner.core.entity.data.GoogleUser
import io.reactivex.Single
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val api: LoginRepository,
    private val pushRegistrationUseCase: PushRegistrationUseCase
) {

    fun login(googleUser: GoogleUser): Single<AuthData> {
        return pushRegistrationUseCase()
            .flatMap { registrationToken ->
                api.login(googleUser.copy(registrationToken = registrationToken))
            }
    }
}
