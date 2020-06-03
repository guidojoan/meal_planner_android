package com.astutify.mealplanner.userprofile.data

import com.astutify.mealplanner.core.authentication.SessionApi
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.entity.data.AuthData
import com.astutify.mealplanner.core.entity.data.HouseEntity
import com.astutify.mealplanner.core.entity.data.error.ApiException
import io.reactivex.Single
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class HouseApiRepository @Inject constructor(
    private val api: HouseApi,
    private val sessionApi: SessionApi,
    private val sessionManager: SessionManager
) {

    fun postHouse(houseEntity: HouseEntity) = api.postHouse(houseEntity)
        .flatMap {
            if (it.isSuccessful) {
                just(Unit)
            } else {
                when (it.code()) {
                    403 -> error(ApiException.ForbiddenException)
                    else -> error(ApiException.InternalServerError)
                }
            }
        }

    fun searchHouse(name: String) = api.searchHouse(name)
        .flatMap {
            if (it.isSuccessful) {
                just(it.body())
            } else {
                when (it.code()) {
                    403 -> error(ApiException.ForbiddenException)
                    else -> error(ApiException.InternalServerError)
                }
            }
        }

    fun linkHouse(houseId: String, joinCode: Int) = api.linkHouse(houseId, joinCode)
        .flatMap {
            if (it.isSuccessful) {
                just(Unit)
            } else {
                when (it.code()) {
                    404 -> error(ApiException.NotFoundException)
                    else -> error(ApiException.InternalServerError)
                }
            }
        }

    fun unlinkHouse() = api.unlinkHouse()
        .flatMap {
            if (it.isSuccessful) {
                just(Unit)
            } else {
                when (it.code()) {
                    403 -> error(ApiException.ForbiddenException)
                    else -> error(ApiException.InternalServerError)
                }
            }
        }

    fun getHouse() = api.getHouse()
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(ApiException.InternalServerError)
            }
        }
        .doOnError {
            it.toString()
        }

    fun getUserProfile() = api.getUserProfile()
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(ApiException.InternalServerError)
            }
        }
        .doOnError {
            it.toString()
        }

    fun getAbout() = api.getAbout()
        .flatMap {
            if (it.isSuccessful) {
                just(it.body()!!)
            } else {
                error(ApiException.InternalServerError)
            }
        }
        .doOnError {
            it.toString()
        }

    fun logout(): Single<Unit> {
        val authData = AuthData(
            sessionManager.getToken(),
            sessionManager.getRefreshToken()
        )
        return sessionApi.logout(authData)
            .map<Unit> {
                if (it.isSuccessful) {
                    sessionManager.logout()
                    Unit
                } else {
                    throw ApiException.AuthException
                }
            }
    }
}
