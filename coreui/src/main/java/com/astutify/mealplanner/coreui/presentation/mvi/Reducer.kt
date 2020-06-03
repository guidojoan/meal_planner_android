package com.astutify.mealplanner.coreui.presentation.mvi

abstract class Reducer<State, Event, Effect> {

    abstract operator fun invoke(state: State, event: Event): ReducerResult<State, Effect>
}
