package com.astutify.mealplanner.userprofile.presentation.profile

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewEvent
import com.astutify.mealplanner.userprofile.presentation.profile.mvi.UserProfileViewFeature
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class UserProfileViewController @Inject constructor(
    private val view: UserProfileView,
    private val feature: UserProfileViewFeature
) : DefaultLifecycleObserver {

    private val disposable = CompositeDisposable()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        disposable.add(
            Observable.wrap(feature)
                .subscribe(
                    view::render
                ) {
                    throw RuntimeException(it)
                }
        )

        disposable.add(
            Observable.wrap(view.events)
                .subscribe(
                    {
                        when (it) {
                            UserProfileView.Intent.ClickRetry -> feature.accept(UserProfileViewEvent.LoadData)
                            UserProfileView.Intent.ClickLogout -> feature.accept(
                                UserProfileViewEvent.LogOut
                            )
                            UserProfileView.Intent.ClickLeaveHouse -> feature.accept(
                                UserProfileViewEvent.LeaveHouse
                            )
                            UserProfileView.Intent.LongClickCard -> feature.accept(
                                UserProfileViewEvent.ClickCard
                            )
                        }
                    },
                    {
                        throw RuntimeException(it)
                    }
                )
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable.dispose()
        feature.dispose()
    }
}
