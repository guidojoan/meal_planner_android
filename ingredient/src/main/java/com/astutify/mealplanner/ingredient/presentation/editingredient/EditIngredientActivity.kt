package com.astutify.mealplanner.ingredient.presentation.editingredient

import android.os.Bundle
import android.text.Editable
import androidx.appcompat.app.AppCompatActivity
import com.astutify.mealplanner.coreui.model.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.model.IngredientMeasurementViewModel
import com.astutify.mealplanner.coreui.model.IngredientPackageViewModel
import com.astutify.mealplanner.coreui.model.IngredientViewModel
import com.astutify.mealplanner.coreui.model.MeasurementViewModel
import com.astutify.mealplanner.coreui.presentation.utils.AfterTextChangeWatcher
import com.astutify.mealplanner.coreui.presentation.control.ItemWithMeasurementCollection
import com.astutify.mealplanner.coreui.presentation.control.hideButton
import com.astutify.mealplanner.coreui.presentation.control.showButton
import com.astutify.mealplanner.coreui.presentation.mvi.Feature
import com.astutify.mealplanner.ingredient.IngredientComponentProvider
import com.astutify.mealplanner.ingredient.R
import com.astutify.mealplanner.ingredient.databinding.ViewEditIngredientBinding
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_edit_ingredient.*
import javax.inject.Inject

class EditIngredientActivity :
    AppCompatActivity(),
    EditIngredientView,
    AddPackageDialog.AddPackageDialogListener,
    AddCustomMeasurementDialog.AddCustomMeasurementDialogListener {

    @Inject
    lateinit var controller: EditIngredientViewController

    private val ingredient: IngredientViewModel? by lazy {
        intent.getParcelableExtra<IngredientViewModel>(INGREDIENT_EXTRA)
    }

    private val eventsRelay: Relay<EditIngredientView.Intent> = PublishRelay.create()

    override val events: Observable<EditIngredientView.Intent>
        get() = eventsRelay

    private lateinit var view: ViewEditIngredientBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ViewEditIngredientBinding.inflate(layoutInflater)
        setContentView(view.root)

        (application as IngredientComponentProvider)
            .ingredientComponent
            .editIngredientActivityBuilder()
            .withActivity(this)
            .withIngredient(ingredient)
            .withInitialState(getInitialState(savedInstanceState))
            .with(this)
            .build()
            .inject(this)

        lifecycle.addObserver(controller)
        initView()
    }

    private fun getInitialState(savedInstanceState: Bundle?): EditIngredientViewState? {
        return savedInstanceState?.getParcelable(Feature.FEATURE_SAVED_STATE)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        controller.onSaveInstance(outState)
    }

    private fun initView() {
        view.packageSelector.setListener {
            when (it) {
                ItemWithMeasurementCollection.Event.OnAddClicked -> showAddPackageDialog()
                is ItemWithMeasurementCollection.Event.OnRemoveClicked -> eventsRelay.accept(
                    EditIngredientView.Intent.PackageRemoved(
                        (it.item as IngredientPackageViewModel).id
                    )
                )
            }
        }

        view.customMeasurementSelector.setListener {
            when (it) {
                ItemWithMeasurementCollection.Event.OnAddClicked ->
                    eventsRelay.accept(EditIngredientView.Intent.ClickAddCustomMeasurement)
                is ItemWithMeasurementCollection.Event.OnRemoveClicked -> eventsRelay.accept(
                    EditIngredientView.Intent.CustomMeasurementRemoved(
                        (it.item as IngredientMeasurementViewModel).measurement
                    )
                )
            }
        }

        view.ingredientName.addTextChangedListener(object : AfterTextChangeWatcher() {
            override fun afterTextChanged(text: Editable) {
                eventsRelay.accept(EditIngredientView.Intent.NameChanged(text.toString()))
            }
        })

        view.categorySelector.setListener {
            (it as? IngredientCategoryViewModel)?.let { category ->
                eventsRelay.accept(
                    EditIngredientView.Intent.CategorySelected(category)
                )
            }
        }

        view.measurementSelector.setListener {
            (it as? MeasurementViewModel)?.let { category ->
                eventsRelay.accept(
                    EditIngredientView.Intent.MeasurementSelected(
                        category
                    )
                )
            }
        }

        view.saveButton.setOnClickListener {
            eventsRelay.accept(EditIngredientView.Intent.ClickSave)
        }

        view.toolbar.setNavigationOnClickListener {
            eventsRelay.accept(EditIngredientView.Intent.ClickBack)
        }
    }

    override fun render(viewState: EditIngredientViewState) {
        renderView(viewState)
        renderLoading(viewState)
        renderError(viewState)
        renderDialog(viewState)
    }

    private fun renderLoading(viewState: EditIngredientViewState) {
        when (viewState.loading) {
            EditIngredientViewState.Loading.LOADING -> view.loadingView.renderLoading()
            EditIngredientViewState.Loading.LOADING_SAVE -> view.loadingView.renderPartialLoading()
            else -> view.loadingView.hide()
        }
    }

    private fun renderError(viewState: EditIngredientViewState) {
        view.ingredientName.error = null
        when (viewState.error) {
            EditIngredientViewState.Error.ERROR_LOADING -> view.messageView.renderNetWorkError()
            EditIngredientViewState.Error.ERROR_SAVE -> view.messageView.renderSnackBarMessage(
                getString(
                    R.string.generic_network_error
                ),
                view.mainContainer
            )
            EditIngredientViewState.Error.ERROR_NAME_TAKEN -> {
                view.ingredientName.error = getString(R.string.error_name_taken)
                view.messageView.hide()
            }
            else -> {
                view.messageView.hide()
            }
        }
    }

    private fun renderDialog(viewState: EditIngredientViewState) {
        when (viewState.dialog) {
            EditIngredientViewState.Dialog.ADD_CUSTOM_MEASUREMENT -> showAddCustomMeasurementDialog(
                ArrayList(viewState.customMeasurements),
                viewState.ingredient.getPrimaryMeasurement()
            )
            else -> {
            }
        }
    }

    private fun renderView(viewState: EditIngredientViewState) {
        if (view.toolbar.title.isNullOrBlank()) {
            view.toolbar.title = when (viewState.mode) {
                EditIngredientViewState.Mode.NEW -> getString(R.string.add_ingredient)
                EditIngredientViewState.Mode.EDIT -> getString(R.string.edit_ingredient)
            }
        }
        if (view.ingredientName.text!!.isBlank() && viewState.ingredient.name.isNotBlank()) {
            view.ingredientName.setText(viewState.ingredient.name)
        }
        if (viewState.saveEnabled) saveButton.showButton() else saveButton.hideButton()
        renderCategorySelector(viewState)
        renderMeasurementSelector(viewState)
        renderCustomMeasurementSelector(viewState)
        renderPackageSelector(viewState)
    }

    private fun renderCategorySelector(viewState: EditIngredientViewState) {
        if (viewState.categories.isNotEmpty() && !view.categorySelector.hasItems()) {
            view.categorySelector.setOptions(
                viewState.categories,
                viewState.ingredient.category
            )
        }
    }

    private fun renderPackageSelector(viewState: EditIngredientViewState) {
        viewState.ingredient.packages?.let {
            view.packageSelector.render(it, viewState.ingredient.getPrimaryMeasurement())
        }
    }

    private fun renderMeasurementSelector(viewState: EditIngredientViewState) {
        if (viewState.measurements.isNotEmpty() && !view.measurementSelector.hasItems()) {
            view.measurementSelector.setOptions(
                viewState.measurements,
                viewState.ingredient.getPrimaryMeasurement()
            )
        }
    }

    private fun renderCustomMeasurementSelector(viewState: EditIngredientViewState) {
        view.customMeasurementSelector.apply {
            render(
                viewState.ingredient.getCustomMeasurements(),
                viewState.ingredient.getPrimaryMeasurement()
            )
            addEnabled(viewState.customMeasurements.isNotEmpty())
        }
    }

    private fun showAddPackageDialog() {
        val dialog = AddPackageDialog.newInstance()
        dialog.show(supportFragmentManager, ADD_PACKAGE_DIALOG)
    }

    private fun showAddCustomMeasurementDialog(
        measurements: ArrayList<MeasurementViewModel>,
        primaryMeasurement: MeasurementViewModel?
    ) {
        val dialog = AddCustomMeasurementDialog.newInstance(measurements, primaryMeasurement)
        dialog.show(supportFragmentManager, ADD_CUSTOM_MEASUREMENT_DIALOG)
    }

    override fun onAddPackage(name: String, quantity: Float) {
        eventsRelay.accept(EditIngredientView.Intent.PackageAdded(name, quantity))
    }

    override fun addCustomMeasurement(measurement: MeasurementViewModel, quantity: Float) {
        eventsRelay.accept(EditIngredientView.Intent.CustomMeasurementAdded(measurement, quantity))
    }

    companion object {
        private const val ADD_PACKAGE_DIALOG = "addPackageDialog"
        private const val ADD_CUSTOM_MEASUREMENT_DIALOG = "addCustomMeasurementDialog"
        const val INGREDIENT_EXTRA = "ingredientExtra"
    }

    @Subcomponent
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: EditIngredientView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            @BindsInstance
            fun withIngredient(view: IngredientViewModel?): Builder

            @BindsInstance
            fun withInitialState(state: EditIngredientViewState?): Builder

            fun build(): Component
        }

        fun inject(activity: EditIngredientActivity)
    }
}
