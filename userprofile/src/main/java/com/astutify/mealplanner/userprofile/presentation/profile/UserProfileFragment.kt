package com.astutify.mealplanner.userprofile.presentation.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.astutify.mealplanner.coreui.model.AboutViewModel
import com.astutify.mealplanner.coreui.presentation.view.AlertMessageView
import com.astutify.mealplanner.userprofile.BuildConfig
import com.astutify.mealplanner.userprofile.R
import com.astutify.mealplanner.userprofile.UserProfileComponentProvider
import com.astutify.mealplanner.userprofile.UserProfileOutNavigatorModule
import com.astutify.mealplanner.userprofile.databinding.ViewUserProfileBinding
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class UserProfileFragment : Fragment(), UserProfileView {

    @Inject
    lateinit var controller: UserProfileViewController

    @Inject
    lateinit var alertMessageView: AlertMessageView

    lateinit var view: ViewUserProfileBinding

    private val eventsRelay: Relay<UserProfileView.Intent> = PublishRelay.create()

    override val events: Observable<UserProfileView.Intent>
        get() = eventsRelay

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        view = ViewUserProfileBinding.inflate(layoutInflater)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity!!.application as UserProfileComponentProvider)
            .userProfileComponent
            .userProfileFragmentBuilder()
            .withActivity(activity!! as AppCompatActivity)
            .with(this)
            .build()
            .inject(this)

        lifecycle.addObserver(controller)

        initView()
    }

    private fun initView() {
        view.toolbar.inflateMenu(R.menu.user_profile)
        view.toolbar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        view.buttonLeaveHouse.setOnClickListener {
            eventsRelay.accept(UserProfileView.Intent.ClickLeaveHouse)
        }
        view.messageView.setListener {
            eventsRelay.accept(UserProfileView.Intent.ClickRetry)
        }
        view.card.setOnLongClickListener {
            eventsRelay.accept(UserProfileView.Intent.LongClickCard)
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> eventsRelay.accept(UserProfileView.Intent.ClickLogout)
        }
        return true
    }

    override fun render(viewState: UserProfileViewState) {
        renderContent(viewState)
        renderLoading(viewState)
        renderError(viewState)
    }

    private fun renderContent(viewState: UserProfileViewState) {
        viewState.user?.let {
            view.tvUserName.text = it.name
            it.email?.let { view.email.text = it }
        }

        viewState.about?.let {
            showAbout(it)
        }

        viewState.houses?.let {
            view.letterContent.text = it.name.take(1)
            view.houseName.text = it.name
            view.joinCode.text = getString(R.string.user_profile_join_code, it.joinCode.toString())
        }
    }

    private fun renderLoading(viewState: UserProfileViewState) {
        when (viewState.loading) {
            UserProfileViewState.Loading.LOADING -> view.loadingView.renderLoading()
            UserProfileViewState.Loading.LOADING_LOGOUT -> view.loadingView.renderPartialLoading()
            UserProfileViewState.Loading.LOADING_LEAVE_HOUSE -> view.buttonLeaveHouse.showLoading()
            else -> view.loadingView.hide()
        }
    }

    private fun renderError(viewState: UserProfileViewState) {
        when (viewState.error) {
            UserProfileViewState.Error.LOADING_ERROR -> view.messageView.renderNetWorkError()
            UserProfileViewState.Error.LOGOUT_ERROR -> showNetworkError()
            UserProfileViewState.Error.LEAVE_HOUSE_ERROR -> {
                showNetworkError()
                view.buttonLeaveHouse.hideLoading()
            }
            else -> view.messageView.hide()
        }
    }

    private fun showAbout(about: AboutViewModel) {
        alertMessageView.showAlert(
            getString(R.string.about),
            message = getString(
                R.string.about_body,
                about.backendVersion,
                BuildConfig.VERSION_NAME
            ),
            positiveButtonLabel = getString(R.string.accept),
            positiveAction = {}
        )
    }

    private fun showNetworkError() {
        view.messageView.renderSnackBarMessage(
            getString(R.string.generic_network_error),
            view.mainContainer
        )
    }

    @Subcomponent(modules = [UserProfileOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: UserProfileView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            fun build(): Component
        }

        fun inject(fragment: UserProfileFragment)
    }
}
