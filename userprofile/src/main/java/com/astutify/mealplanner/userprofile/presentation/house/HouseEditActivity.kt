package com.astutify.mealplanner.userprofile.presentation.house

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.core.extension.getAsInt
import com.astutify.mealplanner.coreui.entity.HouseViewModel
import com.astutify.mealplanner.userprofile.R
import com.astutify.mealplanner.userprofile.UserProfileComponentProvider
import com.astutify.mealplanner.userprofile.UserProfileOutNavigatorModule
import com.astutify.mealplanner.userprofile.databinding.ViewHouseEditBinding
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class HouseEditActivity : AppCompatActivity(), HouseEditView {

    @Inject
    lateinit var controller: HouseEditViewController

    private lateinit var adapter: ArrayAdapter<HouseViewModel>

    private lateinit var view: ViewHouseEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ViewHouseEditBinding.inflate(layoutInflater)
        setContentView(view.rootView)

        (application as UserProfileComponentProvider)
            .userProfileComponent
            .houseEditActivityBuilder()
            .withActivity(this)
            .with(this)
            .build()
            .inject(this)

        initListeners()

        lifecycle.addObserver(controller)
    }

    private fun initListeners() {
        adapter = ArrayAdapter(this, R.layout.item_house, arrayListOf<HouseViewModel>())

        view.houseNameAutocomplete.setAdapter(adapter)
        view.houseNameAutocomplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(name: Editable) {
                eventsRelay.accept(HouseEditView.Intent.OnNameChanged(name.toString()))
            }

            override fun beforeTextChanged(
                name: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

    override fun render(viewState: HouseEditViewState) {
        renderInputText(viewState)
        renderHouses(viewState.houses)
        renderButtonContinue(viewState)
        renderError(viewState)
    }

    private fun renderError(viewState: HouseEditViewState) {
        view.joinCode.error = null
        when (viewState.error) {
            HouseEditViewState.Error.NETWORK -> showNetworkError()
            HouseEditViewState.Error.JOIN_CODE -> showJoinCodeError()
            else -> {
            }
        }
    }

    private fun showNetworkError() {
        Snackbar.make(view.rootView, R.string.generic_network_error, Snackbar.LENGTH_SHORT).show()
    }

    private fun showJoinCodeError() {
        view.joinCode.error = getString(R.string.error_join_code)
    }

    private fun renderInputText(viewState: HouseEditViewState) {
        val isLoading = viewState.loading == HouseEditViewState.Loading.LOADING
        view.joinCodeLayout.apply {
            visibility = if (viewState.joinCodeVisible) View.VISIBLE else View.GONE
            isEnabled = !isLoading
        }
        view.houseNameLayout.apply {
            isEnabled = !isLoading
        }
    }

    private fun renderButtonContinue(viewState: HouseEditViewState) {
        if (viewState.loading == HouseEditViewState.Loading.LOADING) {
            view.buttonContinue.showLoading()
        } else {
            view.buttonContinue.hideLoading()
        }
        view.buttonContinue.isEnabled = viewState.continueEnabled
    }

    private fun renderHouses(list: List<HouseViewModel>) {
        adapter.clear()
        adapter.addAll(list)
        adapter.notifyDataSetChanged()
    }

    private val eventsRelay: Relay<HouseEditView.Intent> = PublishRelay.create()

    override val events: Observable<HouseEditView.Intent>
        get() = Observable.merge(
            eventsRelay,
            view.joinCode.afterTextChangeEvents()
                .map { HouseEditView.Intent.OnJoinCodeChanged(it.editable!!.getAsInt()) },
            view.buttonContinue.clicks().map { HouseEditView.Intent.OnContinueClicked }
        )

    @Subcomponent(modules = [UserProfileOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: HouseEditView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            fun build(): Component
        }

        fun inject(activity: HouseEditActivity)
    }
}
