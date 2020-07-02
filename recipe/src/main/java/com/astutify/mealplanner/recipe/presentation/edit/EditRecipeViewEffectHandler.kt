package com.astutify.mealplanner.recipe.presentation.edit

import com.astutify.mealplanner.core.domain.GetRecipeCategoriesUseCase
import com.astutify.mealplanner.core.entity.data.error.BadRequest
import com.astutify.mealplanner.coreui.model.ActivityResult
import com.astutify.mealplanner.coreui.model.RecipeViewModel
import com.astutify.mealplanner.coreui.model.mapper.toDomain
import com.astutify.mealplanner.coreui.model.mapper.toPresentation
import com.astutify.mealplanner.coreui.presentation.mvi.EffectHandler
import com.astutify.mealplanner.recipe.Navigator
import com.astutify.mealplanner.recipe.RecipeOutNavigator
import com.astutify.mealplanner.recipe.domain.DeleteRecipesUseCase
import com.astutify.mealplanner.recipe.domain.SaveRecipeUseCase
import com.astutify.mealplanner.recipe.domain.UpdateRecipeUseCase
import io.reactivex.Flowable
import io.reactivex.Flowable.never
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

class EditRecipeViewEffectHandler @Inject constructor(
    @Named("ui_thread") private val main: Scheduler,
    private val navigator: Navigator,
    private val outNavigator: RecipeOutNavigator,
    private val saveRecipeUseCase: SaveRecipeUseCase,
    private val getRecipeCategoriesUseCase: GetRecipeCategoriesUseCase,
    private val updateRecipeUseCase: UpdateRecipeUseCase,
    private val deleteRecipesUseCase: DeleteRecipesUseCase
) : EffectHandler<EditRecipeViewState, EditRecipeViewEvent, EditRecipeViewEffect> {

    override fun invoke(
        state: EditRecipeViewState,
        effect: EditRecipeViewEffect
    ): Flowable<EditRecipeViewEvent> {
        return when (effect) {
            is EditRecipeViewEffect.SaveRecipe -> {
                when (state.mode) {
                    EditRecipeViewState.Mode.NEW -> addRecipe(effect.recipe)
                    EditRecipeViewState.Mode.EDIT -> updateRecipe(effect.recipe)
                }
            }
            is EditRecipeViewEffect.GoToRecipeIngredients -> {
                outNavigator.goToAddRecipeIngredient(effect.ingredientGroupId)
                never<EditRecipeViewEvent>()
            }
            EditRecipeViewEffect.LoadData -> {
                getRecipeCategoriesUseCase()
                    .toFlowable()
                    .observeOn(main)
                    .map<EditRecipeViewEvent> {
                        EditRecipeViewEvent.DataLoaded(it.map { it.toPresentation() })
                    }
                    .onErrorReturn {
                        EditRecipeViewEvent.LoadingError
                    }
                    .startWith(EditRecipeViewEvent.Loading)
            }
            EditRecipeViewEffect.GoBack -> {
                navigator.goBack()
                never<EditRecipeViewEvent>()
            }
            EditRecipeViewEffect.Delete -> {
                deleteRecipesUseCase(state.recipe.id)
                    .toFlowable()
                    .observeOn(main)
                    .flatMap<EditRecipeViewEvent> {
                        navigator.finishEditRecipe(
                            ActivityResult(
                                state.recipe.copy(status = RecipeViewModel.Status.DELETED)
                            )
                        )
                        never<EditRecipeViewEvent>()
                    }.onErrorReturn {
                        EditRecipeViewEvent.OnSaveError
                    }
                    .startWith(EditRecipeViewEvent.LoadingSave)
            }
        }
    }

    private fun updateRecipe(recipeViewModel: RecipeViewModel) =
        updateRecipeUseCase(recipeViewModel.toDomain())
            .toFlowable()
            .observeOn(main)
            .flatMap<EditRecipeViewEvent> {
                navigator.finishEditRecipe(
                    ActivityResult(
                        it.toPresentation().copy(status = RecipeViewModel.Status.UPDATED)
                    )
                )
                never<EditRecipeViewEvent>()
            }.onErrorReturn {
                if (it is BadRequest && it.isNameTaken()) {
                    EditRecipeViewEvent.ErrorNameTaken
                } else {
                    EditRecipeViewEvent.OnSaveError
                }
            }
            .startWith(EditRecipeViewEvent.LoadingSave)

    private fun addRecipe(recipeViewModel: RecipeViewModel) =
        saveRecipeUseCase(recipeViewModel.toDomain())
            .toFlowable()
            .observeOn(main)
            .flatMap<EditRecipeViewEvent> {
                navigator.finishEditRecipe(
                    ActivityResult(
                        it.toPresentation().copy(status = RecipeViewModel.Status.NEW)
                    )
                )
                never<EditRecipeViewEvent>()
            }.onErrorReturn {
                if (it is BadRequest && it.isNameTaken()) {
                    EditRecipeViewEvent.ErrorNameTaken
                } else {
                    EditRecipeViewEvent.OnSaveError
                }
            }
            .startWith(EditRecipeViewEvent.LoadingSave)
}
