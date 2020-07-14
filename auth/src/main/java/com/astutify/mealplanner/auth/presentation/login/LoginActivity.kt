package com.astutify.mealplanner.auth.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.auth.AuthComponentProvider
import com.astutify.mealplanner.auth.AuthOutNavigatorModule
import com.astutify.mealplanner.auth.BuildConfig
import com.astutify.mealplanner.auth.R
import com.astutify.mealplanner.auth.databinding.ViewLoginBinding
import com.astutify.mealplanner.auth.presentation.login.mvi.LoginViewController
import com.astutify.mealplanner.auth.presentation.login.mvi.LoginViewState
import com.astutify.mealplanner.core.model.data.GoogleUser
import com.astutify.mealplanner.coreui.presentation.view.AlertMessageView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class LoginActivity : AppCompatActivity(), LoginView {

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private lateinit var view: ViewLoginBinding

    @Inject
    lateinit var controller: LoginViewController

    @Inject
    lateinit var alert: AlertMessageView

    private val eventsRelay: Relay<LoginView.Intent> = PublishRelay.create()

    override val events: Observable<LoginView.Intent>
        get() = eventsRelay

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ViewLoginBinding.inflate(layoutInflater)
        setContentView(view.root)

        (application as AuthComponentProvider)
            .authComponent
            .loginActivityBuilder()
            .withActivity(this)
            .withView(this)
            .build()
            .inject(this)

        lifecycle.addObserver(controller)
        configureGoogleSignIn()
    }

    private fun configureGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_API_ID)
            .requestEmail()
            .requestProfile()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        view.loginButton.setOnClickListener {

            val signInIntent = mGoogleSignInClient!!.signInIntent
            startActivityForResult(
                signInIntent,
                RC_SIGN_IN
            )
        }
        view.loginButton.visibility = View.VISIBLE
    }

    override fun render(viewState: LoginViewState) {
        view.loading.visibility = if (viewState.loading) View.VISIBLE else View.GONE

        when (viewState.error) {
            LoginViewState.Error.LOGIN -> showLoginError()
            LoginViewState.Error.NETWORK_CONNECTION -> showGenericNetworkError()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {

                val account = task.getResult(ApiException::class.java)
                val user = GoogleUser(
                    account!!.id!!,
                    account.displayName!!,
                    account.email!!,
                    account.serverAuthCode!!
                )
                eventsRelay.accept(
                    LoginView.Intent.Login(
                        user
                    )
                )
            } catch (e: ApiException) {
                eventsRelay.accept(LoginView.Intent.GoogleSignInError)
            }
        }
    }

    private fun showGenericNetworkError() {
        alert.showAlert(
            title = getString(R.string.error),
            message = getString(R.string.generic_network_error),
            positiveButtonLabel = getString(R.string.accept),
            positiveAction = {
                it.dismiss()
            }
        )
    }

    private fun showLoginError() {
        alert.showAlert(
            title = getString(R.string.error),
            message = getString(R.string.login_error),
            positiveButtonLabel = getString(R.string.accept),
            positiveAction = {
                it.dismiss()
            }
        )
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

    @Subcomponent(modules = [AuthOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun withView(view: LoginView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            fun build(): Component
        }

        fun inject(activity: LoginActivity)
    }
}
