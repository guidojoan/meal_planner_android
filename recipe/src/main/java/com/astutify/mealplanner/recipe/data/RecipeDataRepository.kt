package com.astutify.mealplanner.recipe.data

import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.core.entity.data.RecipeEntity
import com.astutify.mealplanner.core.entity.data.mapper.toDomain
import com.astutify.mealplanner.core.entity.domain.IngredientCategory
import com.astutify.mealplanner.core.entity.domain.Measurement
import com.astutify.mealplanner.core.entity.domain.Recipe
import com.astutify.mealplanner.core.entity.domain.RecipeCategory
import com.astutify.mealplanner.core.entity.domain.mapper.toData
import com.astutify.mealplanner.recipe.domain.RecipeRepository
import io.reactivex.Single
import io.reactivex.functions.Function3
import javax.inject.Inject

class RecipeDataRepository @Inject constructor(
    private val adminApiRepository: RecipeAdminApiRepository,
    private val recipesApiRepository: RecipesApiRepository,
    private val categoriesDataRepository: CategoriesDataRepository,
    private val measurementDataRepository: MeasurementDataRepository
) : RecipeRepository {

    override fun getRecipesFirstPage(keywords: String?) =
        mapRecipes(recipesApiRepository.firstPage(keywords))

    override fun getRecipesNextPage() =
        mapRecipes(recipesApiRepository.nextPage())

    override fun saveRecipe(recipe: Recipe) =
        mapRecipe(adminApiRepository.saveRecipe(recipe.toData()))

    override fun updateRecipe(recipe: Recipe) =
        mapRecipe(adminApiRepository.updateRecipe(recipe.toData()))

    override fun deleteRecipe(recipeId: String) =
        adminApiRepository.deleteRecipe(recipeId)

    private fun mapRecipes(recipes: Single<List<RecipeEntity>>) =
        recipes
            .flatMap {
                Single.zip(
                    measurementDataRepository.getMeasurements(),
                    categoriesDataRepository.getRecipeCategories(),
                    categoriesDataRepository.getIngredientCategories(),
                    Function3 { measurements: List<Measurement>, recipeCategories: List<RecipeCategory>, ingredientCategories: List<IngredientCategory> ->
                        it.map {
                            it.toDomain(
                                measurements,
                                recipeCategories,
                                ingredientCategories
                            )
                        }
                    }
                )
            }

    private fun mapRecipe(recipe: Single<RecipeEntity>) =
        recipe
            .flatMap {
                Single.zip(
                    measurementDataRepository.getMeasurements(),
                    categoriesDataRepository.getRecipeCategories(),
                    categoriesDataRepository.getIngredientCategories(),
                    Function3 { measurements: List<Measurement>, recipeCategories: List<RecipeCategory>, ingredientCategories: List<IngredientCategory> ->
                        it.toDomain(
                            measurements,
                            recipeCategories,
                            ingredientCategories
                        )
                    }
                )
            }
}
