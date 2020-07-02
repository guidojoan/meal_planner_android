package com.astutify.mealplanner.core.authentication

import com.astutify.mealplanner.core.model.data.AuthData
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class MyAuthenticator @Inject constructor(
    private val sessionApi: SessionApi,
    private val sessionManager: SessionManager
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {

        if (responseCount(response) >= MAX_RETRIES) {
            return null
        }

        val authResponse = sessionApi.refreshToken(
            AuthData(
                token = sessionManager.getToken(),
                refreshToken = sessionManager.getRefreshToken()
            )
        ).execute()

        return if (authResponse.isSuccessful) {

            authResponse.body()?.let { authResponseData ->
                sessionManager.setTokens(authResponseData.token, authResponseData.refreshToken)
                response.request.newBuilder()
                    .header(AUTH_HEADER, authResponseData.token)
                    .build()
            }
        } else {
            return null
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        while (response.priorResponse != null) {
            result++
        }
        return result
    }

    companion object {
        private const val AUTH_HEADER = "x-access-token"
        private const val MAX_RETRIES = 3
    }
}
