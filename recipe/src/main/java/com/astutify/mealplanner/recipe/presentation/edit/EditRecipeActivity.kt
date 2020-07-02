package com.astutify.mealplanner.recipe.presentation.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.astutify.mealplanner.AppConstants
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.model.RecipeIngredientViewModel
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.coreui.presentation.view.AlertMessageView
import com.astutify.mealplanner.coreui.presentation.control.ChipCollectionSingleSelection
import com.astutify.mealplanner.coreui.presentation.control.NumberPicker
import com.astutify.mealplanner.coreui.presentation.view.LoadingView
import com.astutify.mealplanner.coreui.presentation.view.MessageView
import com.astutify.mealplanner.coreui.presentation.utils.ImagePickerUtils
import com.astutify.mealplanner.recipe.R
import com.astutify.mealplanner.recipe.RecipeComponentProvider
import com.astutify.mealplanner.recipe.RecipeOutNavigatorModule
import com.astutify.mealplanner.recipe.presentation.edit.adapter.RecipeIngredientsListView
import com.bumptech.glide.Glide
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.jakewharton.rxbinding3.view.clicks
import com.jakewharton.rxbinding3.widget.afterTextChangeEvents
import com.jakewharton.rxrelay2.PublishRelay
import com.jakewharton.rxrelay2.Relay
import dagger.BindsInstance
import dagger.Subcomponent
import io.reactivex.Observable
import javax.inject.Inject

class EditRecipeActivity :
    AppCompatActivity(),
    EditRecipeView,
    AddIngredientGroupDialog.AddIngredientGroupDialogListener {

    @Inject
    lateinit var controller: EditRecipeViewController

    @Inject
    lateinit var imagePickerUtils: ImagePickerUtils

    @Inject
    lateinit var alertMessage: AlertMessageView

    private val eventsRelay: Relay<EditRecipeView.Intent> = PublishRelay.create()

    private lateinit var directions: EditText
    private lateinit var recipeName: EditText
    private lateinit var saveButton: ExtendedFloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var collapsingToolbar: CollapsingToolbarLayout
    private lateinit var recipePicture: ImageView
    private lateinit var servings: NumberPicker
    private lateinit var categorySelection: ChipCollectionSingleSelection
    private lateinit var ingredientsBottomSheet: MaterialCardView
    private lateinit var recipeIngredientsList: RecipeIngredientsListView
    private lateinit var loadingView: LoadingView
    private lateinit var mainContainer: CoordinatorLayout
    private lateinit var messageView: MessageView

    override val events: Observable<EditRecipeView.Intent>
        get() = Observable.merge(
            eventsRelay,
            directions.afterTextChangeEvents().map {
                EditRecipeView.Intent.DirectionsChanged(
                    it.editable.toString()
                )
            },
            recipeName.afterTextChangeEvents().map {
                EditRecipeView.Intent.NameChanged(
                    it.editable.toString()
                )
            },
            saveButton.clicks().map {
                EditRecipeView.Intent.SaveClick
            }
        )

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_edit_recipe)

        val recipe = intent.getParcelableExtra<RecipeViewModel>(RECIPE_EXTRA)

        (application as RecipeComponentProvider)
            .recipeComponent
            .editRecipeActivityBuilder()
            .withActivity(this)
            .withRecipe(recipe)
            .with(this)
            .build()
            .inject(this)

        bindViews()
        initToolbar(recipe != null)
        setupListeners()

        lifecycle.addObserver(controller)
    }

    private fun bindViews() {
        directions = findViewById(R.id.directions)
        recipeName = findViewById(R.id.recipeName)
        saveButton = findViewById(R.id.saveButton)
        toolbar = findViewById(R.id.toolbar)
        collapsingToolbar = findViewById(R.id.collapsingToolbar)
        recipePicture = findViewById(R.id.recipePicture)
        servings = findViewById(R.id.servings)
        categorySelection = findViewById(R.id.categorySelection)
        ingredientsBottomSheet = findViewById(R.id.ingredientsBottomSheet)
        recipeIngredientsList = findViewById(R.id.recipeIngredientsList)
        loadingView = findViewById(R.id.loadingView)
        mainContainer = findViewById(R.id.mainContainer)
        messageView = findViewById(R.id.messageView)
    }

    private fun initToolbar(editMode: Boolean) {
        if (editMode) {
            toolbar.inflateMenu(R.menu.menu_edit_recipe)
            toolbar.setOnMenuItemClickListener {
                if (it.itemId == R.id.delete_recipe) {
                    eventsRelay.accept(EditRecipeView.Intent.DeleteClicked)
                }
                true
            }
        }
        toolbar.setNavigationOnClickListener {
            eventsRelay.accept(EditRecipeView.Intent.ClickBack)
        }
        collapsingToolbar.setExpandedTitleColor(ContextCompat.getColor(this, R.color.transparent))
    }

    private fun setupListeners() {
        recipePicture.setOnClickListener {
            openGallery()
        }
        servings.setListener {
            eventsRelay.accept(EditRecipeView.Intent.ServingsChanged(it))
        }
        categorySelection.setListener {
            (it as? RecipeCategoryViewModel)?.let { category ->
                eventsRelay.accept(
                    EditRecipeView.Intent.CategorySelected(
                        category
                    )
                )
            }
        }
        bottomSheetBehavior = BottomSheetBehavior.from(ingredientsBottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                        hideSaveButton()
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        showSaveButton()
                    }
                }
            }
        )
    }

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            super.onBackPressed()
        }
    }

    private fun hideSaveButton() {
        saveButton.animate().scaleX(0f).scaleY(0f).setDuration(300).start()
    }

    private fun showSaveButton() {
        saveButton.animate().scaleX(1f).scaleY(1f).setDuration(300).start()
    }

    override fun render(viewState: EditRecipeViewState) {
        renderContent(viewState)
        renderLoading(viewState)
        renderError(viewState)
        renderMessage(viewState)
        saveButton.visibility = if (viewState.saveEnabled) View.VISIBLE else View.INVISIBLE
    }

    private fun renderContent(viewState: EditRecipeViewState) {
        collapsingToolbar.title = viewState.recipe.name

        if (recipeName.text.isNullOrBlank() && viewState.recipe.name.isNotBlank()) {
            recipeName.setText(viewState.recipe.name)
        }

        if (viewState.recipeCategories != null && !categorySelection.hasItems()) {
            categorySelection.setOptions(
                viewState.recipeCategories,
                viewState.recipe.recipeCategory
            )
        }

        servings.setValue(viewState.recipe.servings)

        if (directions.text.isBlank() && viewState.recipe.directions.isNotBlank()) {
            directions.setText(viewState.recipe.directions)
        }

        if (viewState.recipe.imageUrl.isNotBlank()) {
            loadImage(viewState.recipe.imageUrl)
        }

        recipeIngredientsList.bind(viewState.recipe.ingredientGroups) {
            when (it) {
                is RecipeIngredientsListView.Event.AddIngredient -> eventsRelay.accept(
                    EditRecipeView.Intent.NewIngredientClicked(it.ingredientGroupId)
                )
                RecipeIngredientsListView.Event.AddIngredientGroup -> {
                    val dialog = AddIngredientGroupDialog.newInstance()
                    dialog.show(supportFragmentManager, "")
                }
                is RecipeIngredientsListView.Event.RemoveIngredient -> {
                    eventsRelay.accept(EditRecipeView.Intent.IngredientRemoveClicked(it.ingredientId))
                }
                is RecipeIngredientsListView.Event.RemoveIngredientGroup -> {
                    eventsRelay.accept(EditRecipeView.Intent.IngredientGroupRemoveClicked(it.ingredientGroupId))
                }
            }
        }
    }

    private fun renderError(viewState: EditRecipeViewState) {
        recipeName.error = null
        when (viewState.error) {
            EditRecipeViewState.Error.LOADING -> messageView.renderNetWorkError()
            EditRecipeViewState.Error.SAVE -> messageView.renderSnackBarMessage(
                getString(R.string.generic_network_error),
                mainContainer
            )
            EditRecipeViewState.Error.NAME_TAKEN -> {
                recipeName.error = getString(R.string.error_name_taken)
                messageView.hide()
            }
            EditRecipeViewState.Error.IMAGE_NOT_SELECTED -> showImageNotSelectedError()
            else -> messageView.hide()
        }
    }

    private fun renderMessage(viewState: EditRecipeViewState) {
        when (viewState.message) {
            EditRecipeViewState.Message.DELETE_ALERT -> showDeleteAlertMessage()
            else -> {
            }
        }
    }

    private fun renderLoading(viewState: EditRecipeViewState) {
        when (viewState.loading) {
            EditRecipeViewState.Loading.LOADING -> loadingView.renderLoading()
            EditRecipeViewState.Loading.SAVE -> loadingView.renderPartialLoading()
            else -> loadingView.hide()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AddRecipeIngredientRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            data.getParcelableExtra<ActivityResult<RecipeIngredientViewModel>>(AppConstants.ActivityResultObject)
                ?.let {
                    eventsRelay.accept(EditRecipeView.Intent.IngredientAdded(it.result, it.extra))
                }
        }

        if (requestCode == PickImageRequestCode && resultCode == Activity.RESULT_OK && data != null) {
            data.data?.let {
                if (imagePickerUtils.isValidImageType(it)) {
                    loadImage(it.toString())
                    eventsRelay.accept(EditRecipeView.Intent.ImagePicked(it.toString()))
                } else {
                    alertMessage.showAlert(
                        message = getString(R.string.error_image_format),
                        positiveButtonLabel = getString(R.string.accept),
                        positiveAction = {}
                    )
                }
            }
        }
    }

    private fun loadImage(imageUrl: String) {
        Glide.with(this)
            .load(imageUrl)
            .centerCrop()
            .into(recipePicture)
    }

    override fun onAddIngredientGroup(ingredientGroupName: String) {
        eventsRelay.accept(EditRecipeView.Intent.IngredientGroupAdded(ingredientGroupName))
    }

    private fun openGallery() {
        val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        startActivityForResult(gallery, PickImageRequestCode)
    }

    private fun showImageNotSelectedError() {
        alertMessage.showAlert(
            message = getString(R.string.error_image_not_selected),
            positiveButtonLabel = getString(R.string.accept),
            positiveAction = {}
        )
    }

    private fun showDeleteAlertMessage() {
        alertMessage.showAlert(
            title = getString(R.string.delete_recipe),
            message = getString(R.string.message_delete_recipe),
            positiveButtonLabel = getString(R.string.accept),
            positiveAction = {
                eventsRelay.accept(EditRecipeView.Intent.ConfirmDelete)
            },
            negativeButtonLabel = getString(R.string.cancel),
            negativeAction = {}
        )
    }

    @Subcomponent(modules = [RecipeOutNavigatorModule::class])
    interface Component {
        @Subcomponent.Builder
        interface Builder {

            @BindsInstance
            fun with(view: EditRecipeView): Builder

            @BindsInstance
            fun withActivity(view: AppCompatActivity): Builder

            @BindsInstance
            fun withRecipe(recipe: RecipeViewModel?): Builder

            fun build(): Component
        }

        fun inject(activity: EditRecipeActivity)
    }

    companion object {
        const val AddRecipeIngredientRequestCode = 8
        const val PickImageRequestCode = 9
        const val RECIPE_EXTRA = "recipeExtra"
    }
}
