package com.astutify.mealplanner.auth.presentation.login

import com.astutify.mealplanner.auth.AuthOutNavigator
import com.astutify.mealplanner.auth.domain.LoginUseCase
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class LoginViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val authOutNavigator: AuthOutNavigator,
    private val useCase: LoginUseCase
) : EffectHandler<LoginViewState, LoginViewEvent, LoginViewEffect> {

    override fun invoke(state: LoginViewState, effect: LoginViewEffect): Flowable<out LoginViewEvent> {
        return when (effect) {
            is LoginViewEffect.Login -> {
                useCase.login(effect.googleUser)
                    .observeOn(main)
                    .toFlowable()
                    .flatMap<LoginViewEvent> {
                        if (it.houseId != null) {
                            authOutNavigator.goToHome()
                        } else {
                            authOutNavigator.goToHouseEdit()
                        }
                        never()
                    }
                    .onErrorReturn {
                        LoginViewEvent.LoginError
                    }
                    .startWith(LoginViewEvent.Loading)
            }
        }
    }
}
