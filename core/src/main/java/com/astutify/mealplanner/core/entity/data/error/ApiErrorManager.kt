package com.astutify.mealplanner.core.entity.data.error

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import javax.inject.Inject

class ApiErrorManager @Inject constructor() {

    fun mapError(errorCode: Int, errorBody: String?): ApiException {
        return when (errorCode) {
            400 -> {
                BadRequest(mapErrorBody(errorBody))
            }
            404 -> {
                ApiException.NotFoundException
            }
            else -> {
                ApiException.InternalServerError
            }
        }
    }

    private fun mapErrorBody(errorBodyJson: String?): List<ErrorItem> {
        return try {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            val adapter: JsonAdapter<ErrorBody> = moshi.adapter(ErrorBody::class.java)
            val errorBody = adapter.fromJson(errorBodyJson!!)
            val result = mutableListOf<ErrorItem>()
            errorBody?.errors?.forEach {
                result.add(ErrorItem(mapType(it), it.path))
            }
            return result
        } catch (error: Throwable) {
            mutableListOf()
        }
    }

    private fun mapType(errorSpec: ErrorSpec): ErrorType {
        return when (errorSpec.validatorKey) {
            NOT_UNIQUE -> ErrorType.VALUE_TAKEN
            else -> ErrorType.UNDEFINED
        }
    }

    companion object {
        private const val NOT_UNIQUE = "not_unique"
    }
}
