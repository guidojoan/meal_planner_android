package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.model.domain.About
import com.astutify.mealplanner.core.model.domain.House
import com.astutify.mealplanner.core.model.domain.User
import io.reactivex.Single

interface HouseRepository {

    fun createHouse(house: House): Single<Unit>

    fun getHouse(): Single<House>

    fun searchHouse(name: String): Single<List<House>>

    fun linkHouse(houseId: String, joinCode: Int): Single<Unit>

    fun unlinkHouse(): Single<Unit>

    fun logout(): Single<Unit>

    fun getUserProfile(): Single<User>

    fun getAbout(): Single<About>
}
