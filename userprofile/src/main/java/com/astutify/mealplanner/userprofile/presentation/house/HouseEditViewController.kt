package com.astutify.mealplanner.userprofile.presentation.house

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewEvent
import com.astutify.mealplanner.userprofile.presentation.house.mvi.HouseEditViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HouseEditViewController @Inject constructor(
    private val view: HouseEditView,
    private val feature: HouseEditViewFeature
) : DefaultLifecycleObserver {

    private val disposable = CompositeDisposable()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        disposable.add(
            Observable.wrap(feature)
                .distinctUntilChanged()
                .subscribe(
                    view::render
                ) { throw RuntimeException(it) }
        )

        disposable.add(
            Observable.wrap(view.events)
                .subscribe(
                    {
                        when (it) {
                            is HouseEditView.Intent.OnNameChanged -> feature.accept(
                                HouseEditViewEvent.OnNameChanged(it.name)
                            )
                            is HouseEditView.Intent.OnJoinCodeChanged -> feature.accept(HouseEditViewEvent.OnJoinCodeChanged(it.joinCode))
                            HouseEditView.Intent.OnContinueClicked -> feature.accept(HouseEditViewEvent.ContinueClicked)
                        }
                    },
                    { throw RuntimeException(it) }
                )
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable.dispose()
        feature.dispose()
    }
}
