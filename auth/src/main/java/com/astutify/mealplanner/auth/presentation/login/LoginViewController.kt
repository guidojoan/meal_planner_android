package com.astutify.mealplanner.auth.presentation.login

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class LoginViewController @Inject constructor(
    private val view: LoginView,
    private val feature: LoginViewFeature
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
                            is LoginView.Intent.Login -> feature.accept(
                                LoginViewEvent.LoginBackend(
                                    it.googleUser
                                )
                            )
                            is LoginView.Intent.GoogleSignInError -> feature.accept(
                                LoginViewEvent.NetworkConnectionError
                            )
                        }
                    },
                    { throw RuntimeException(it) }
                )
        )
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        disposable.dispose()
    }
}
