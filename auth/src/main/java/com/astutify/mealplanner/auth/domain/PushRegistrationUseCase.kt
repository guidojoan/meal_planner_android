package com.astutify.mealplanner.auth.domain

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import javax.inject.Inject

class PushRegistrationUseCase @Inject constructor() {

    operator fun invoke(): Single<String> {

        return Single.create<String> {
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        it.onError(Throwable())
                    }
                    val token = task.result?.token
                    it.onSuccess(token!!)
                }
        }
    }
}
