package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.Mockable
import com.astutify.mealplanner.core.authentication.SessionManager
import javax.inject.Inject

@Mockable
class LeaveHouseUseCase @Inject constructor(
    private val repository: HouseRepository,
    private val sessionManager: SessionManager
) {
    operator fun invoke() =
        repository.unlinkHouse()
            .map {
                sessionManager.clearHasHouse()
            }
}
