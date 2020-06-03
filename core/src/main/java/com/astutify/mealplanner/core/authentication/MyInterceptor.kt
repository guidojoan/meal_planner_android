package com.astutify.mealplanner.core.authentication

import com.astutify.mealplanner.core.utils.LanguageUtils
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MyInterceptor @Inject constructor(
    private val sessionManager: SessionManager,
    private val languageUtils: LanguageUtils
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        return if (!sessionManager.isLogued()) {
            chain.proceed(originalRequest)
        } else {
            val authorisedRequest = originalRequest.newBuilder()
                .header(AUTH_HEADER, sessionManager.getToken())
                .header(CLIENT_LANGUAGE, languageUtils.getSystemLanguage())
                .build()
            chain.proceed(authorisedRequest)
        }
    }

    companion object {
        private const val AUTH_HEADER = "x-access-token"
        private const val CLIENT_LANGUAGE = "client-lang"
    }
}
