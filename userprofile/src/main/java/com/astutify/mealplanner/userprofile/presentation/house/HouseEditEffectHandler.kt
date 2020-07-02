package com.astutify.mealplanner.userprofile.presentation.house

import com.astutify.mealplanner.core.entity.data.error.ApiException
import com.astutify.mealplanner.coreui.model.HouseViewModel
import com.astutify.mealplanner.coreui.model.mapper.toDomain
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import com.astutify.mealplanner.userprofile.UserProfileOutNavigator
import com.astutify.mealplanner.userprofile.domain.CreateHouseUseCase
import com.astutify.mealplanner.userprofile.domain.LinkHouseUseCase
import com.astutify.mealplanner.userprofile.domain.SearchHouseUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class HouseEditEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val outNavigatorComponent: UserProfileOutNavigator,
    private val searchHouseUseCase: SearchHouseUseCase,
    private val linkHouseUseCase: LinkHouseUseCase,
    private val createHouseUseCase: CreateHouseUseCase
) :
    EffectHandler<HouseEditViewState, HouseEditViewEvent, HouseEditViewEffect> {

    override fun invoke(
        state: HouseEditViewState,
        effect: HouseEditViewEffect
    ): Flowable<out HouseEditViewEvent> {
        return when (effect) {
            is HouseEditViewEffect.SearchHouse -> {
                if (effect.name.length > 3) {
                    searchHouseUseCase(effect.name)
                        .toFlowable()
                        .map<HouseEditViewEvent> {
                            HouseEditViewEvent.DataLoaded(it.map { it.toPresentation() })
                        }
                        .onErrorReturn {
                            HouseEditViewEvent.LoadingError
                        }
                        .observeOn(main)
                } else {
                    never()
                }
            }
            is HouseEditViewEffect.JoinHouse -> {
                linkHouseUseCase(effect.houseId, effect.joinCode)
                    .toFlowable()
                    .flatMap<HouseEditViewEvent> {
                        outNavigatorComponent.goToHome()
                        never()
                    }.onErrorReturn {
                        when (it) {
                            is ApiException.NotFoundException -> HouseEditViewEvent.JoinCodeError
                            else -> HouseEditViewEvent.LoadingError
                        }
                    }
                    .observeOn(main)
                    .startWith(HouseEditViewEvent.Loading)
            }
            is HouseEditViewEffect.CreateHouse -> {
                createHouseUseCase(
                    (HouseViewModel(name = effect.name)).toDomain()
                )
                    .toFlowable()
                    .flatMap<HouseEditViewEvent> {
                        outNavigatorComponent.goToHome()
                        never()
                    }.onErrorReturn {
                        HouseEditViewEvent.LoadingError
                    }
                    .observeOn(main)
                    .startWith(HouseEditViewEvent.Loading)
            }
        }
    }
}
