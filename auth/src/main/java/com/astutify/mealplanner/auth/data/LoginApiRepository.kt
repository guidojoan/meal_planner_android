package com.astutify.mealplanner.auth.data

import com.astutify.mealplanner.core.authentication.SessionApi
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.model.data.AuthData
import com.astutify.mealplanner.core.model.data.GoogleUser
import com.astutify.mealplanner.core.model.data.error.ApiException
import io.reactivex.Single
import javax.inject.Inject

class LoginApiRepository @Inject constructor(
    private val sessionApi: SessionApi,
    private val sessionManager: SessionManager
) {

    fun login(googleUser: GoogleUser): Single<AuthData> {
        return sessionApi.login(googleUser)
            .map {
                if (it.isSuccessful) {
                    val session = it.body()!!
                    sessionManager.setTokens(session.token, session.refreshToken)
                    session.houseId?.let { sessionManager.setHasHouse() }
                    session
                } else {
                    throw ApiException.AuthException
                }
            }
    }
}
