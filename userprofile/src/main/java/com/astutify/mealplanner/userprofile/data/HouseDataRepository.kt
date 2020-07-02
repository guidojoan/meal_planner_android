package com.astutify.mealplanner.userprofile.data

import com.astutify.mealplanner.core.model.data.mapper.toDomain
import com.astutify.mealplanner.core.model.domain.About
import com.astutify.mealplanner.core.model.domain.House
import com.astutify.mealplanner.core.model.domain.User
import com.astutify.mealplanner.core.model.domain.mapper.toData
import com.astutify.mealplanner.userprofile.domain.HouseRepository
import io.reactivex.Single
import javax.inject.Inject

class HouseDataRepository @Inject constructor(
    private val apiRepository: HouseApiRepository
) : HouseRepository {

    override fun createHouse(house: House): Single<Unit> {
        return apiRepository.postHouse(house.toData())
    }

    override fun getHouse(): Single<House> {
        return apiRepository.getHouse()
            .map {
                it.toDomain()
            }
    }

    override fun searchHouse(name: String): Single<List<House>> {
        return apiRepository.searchHouse(name)
            .map {
                it.map { it.toDomain() }
            }
    }

    override fun linkHouse(houseId: String, joinCode: Int): Single<Unit> {
        return apiRepository.linkHouse(houseId, joinCode)
    }

    override fun unlinkHouse(): Single<Unit> {
        return apiRepository.unlinkHouse()
    }

    override fun logout(): Single<Unit> {
        return apiRepository.logout()
    }

    override fun getUserProfile(): Single<User> {
        return apiRepository.getUserProfile()
            .map {
                it.toDomain()
            }
    }

    override fun getAbout(): Single<About> {
        return apiRepository.getAbout()
            .map {
                it.toDomain()
            }
    }
}
