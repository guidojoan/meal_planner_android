package com.astutify.mealplanner.coreui.presentation.mvi

import io.reactivex.Flowable

typealias EffectHandler<State, Event, Effect> = (state: State, effect: Effect) -> Flowable<out Event>
