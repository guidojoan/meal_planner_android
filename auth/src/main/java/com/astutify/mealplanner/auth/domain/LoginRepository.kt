package com.astutify.mealplanner.auth.domain

import com.astutify.mealplanner.core.model.data.AuthData
import com.astutify.mealplanner.core.model.data.GoogleUser
import io.reactivex.Single

interface LoginRepository {

    fun login(googleUser: GoogleUser): Single<AuthData>
}
