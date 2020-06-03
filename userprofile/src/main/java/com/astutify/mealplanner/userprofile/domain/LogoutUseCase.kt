package com.astutify.mealplanner.userprofile.domain

import com.astutify.mealplanner.core.Mockable
import javax.inject.Inject

@Mockable
class LogoutUseCase @Inject constructor(
    private val repository: HouseRepository
) {
    operator fun invoke() = repository.logout()
}
