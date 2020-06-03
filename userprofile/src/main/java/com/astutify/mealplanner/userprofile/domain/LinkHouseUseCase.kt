package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.core.authentication.SessionManager
import javax.inject.Inject

@Mockable
class LinkHouseUseCase @Inject constructor(
    private val repository: HouseRepository,
    private val sessionManager: SessionManager
) {
    operator fun invoke(houseId: String, joinCode: Int) =
        repository.linkHouse(houseId, joinCode)
            .map { sessionManager.setHasHouse() }
}
