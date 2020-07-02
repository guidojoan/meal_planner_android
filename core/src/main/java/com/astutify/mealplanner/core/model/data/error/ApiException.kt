package com.astutify.mealplanner.core.model.data.error

sealed class ApiException : Throwable() {
    object ForbiddenException : ApiException()
    object InternalServerError : ApiException()
    object NetworkConnectionError : ApiException()
    object NotFoundException : ApiException()
    object AuthException : ApiException()
}

data class BadRequest(val errors: List<ErrorItem>) : ApiException() {

    fun isNameTaken(): Boolean {
        errors.forEach {
            if (it.field == NAME_FIELD && it.type == ErrorType.VALUE_TAKEN) {
                return true
            }
        }
        return false
    }

    companion object {
        private const val NAME_FIELD = "name"
    }
}

data class ErrorItem(
    val type: ErrorType,
    val field: String?
)

enum class ErrorType {
    VALUE_TAKEN, UNDEFINED
}
