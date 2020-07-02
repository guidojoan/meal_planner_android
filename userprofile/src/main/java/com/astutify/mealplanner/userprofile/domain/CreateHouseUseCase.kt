package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.model.domain.House
import javax.inject.Inject

@Mockable
class CreateHouseUseCase @Inject constructor(
    private val repository: HouseRepository,
    private val sessionManager: SessionManager
) {
    operator fun invoke(house: House) =
        repository.createHouse(house)
            .map { sessionManager.setHasHouse() }
}
