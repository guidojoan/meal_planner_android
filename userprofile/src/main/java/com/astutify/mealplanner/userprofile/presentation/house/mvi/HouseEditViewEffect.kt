package com.astutify.mealplanner.userprofile.presentation.house.mvi

sealed class HouseEditViewEffect {
    data class SearchHouse(val name: String) : HouseEditViewEffect()
    data class CreateHouse(val name: String) : HouseEditViewEffect()
    data class JoinHouse(val houseId: String, val joinCode: Int) : HouseEditViewEffect()
}
