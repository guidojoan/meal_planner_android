package com.astutify.mealplanner.core.entity.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GoogleUser(
    var userId: String,
    var name: String,
    var email: String,
    var token: String,
    var registrationToken: String = ""
)
