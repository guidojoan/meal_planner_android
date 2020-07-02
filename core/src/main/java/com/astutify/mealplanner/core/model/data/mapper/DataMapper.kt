package com.astutify.mealplanner.core.model.data.mapper

import com.astutify.mealplanner.core.model.data.AboutApi
import com.astutify.mealplanner.core.model.data.HouseApi
import com.astutify.mealplanner.core.model.data.IngredientCategoryApi
import com.astutify.mealplanner.core.model.data.IngredientApi
import com.astutify.mealplanner.core.model.data.MeasurementApi
import com.astutify.mealplanner.core.model.data.MeasurementLocalizationApi
import com.astutify.mealplanner.core.model.data.RecipeCategoryApi
import com.astutify.mealplanner.core.model.data.RecipeApi
import com.astutify.mealplanner.core.model.data.UserApi
import com.astutify.mealplanner.core.model.domain.About
import com.astutify.mealplanner.core.model.domain.House
import com.astutify.mealplanner.core.model.domain.Ingredient
import com.astutify.mealplanner.core.model.domain.IngredientCategory
import com.astutify.mealplanner.core.model.domain.IngredientGroup
import com.astutify.mealplanner.core.model.domain.IngredientMeasurement
import com.astutify.mealplanner.core.model.domain.IngredientPackage
import com.astutify.mealplanner.core.model.domain.Measurement
import com.astutify.mealplanner.core.model.domain.MeasurementLocalization
import com.astutify.mealplanner.core.model.domain.Recipe
import com.astutify.mealplanner.core.model.domain.RecipeCategory
import com.astutify.mealplanner.core.model.domain.RecipeIngredient
import com.astutify.mealplanner.core.model.domain.User
import java.lang.NullPointerException

fun IngredientApi.toDomain(
    measurements: List<Measurement>,
    categories: List<IngredientCategory>
): Ingredient {

    val ingredientMeasurements = this.measurements.map {
        IngredientMeasurement(
            it.id,
            it.quantity,
            measurements.find { measurement -> measurement.id == it.measurementId }?.let { it }
                ?: throw NullPointerException("ingredient measurement cannot be null")
        )
    }

    val category = categories.find { cat -> cat.id == categoryId }?.let { it }
        ?: throw NullPointerException("ingredient category cannot be null")

    val packages = packages?.map { pkg ->
        IngredientPackage(
            pkg.id,
            pkg.name,
            pkg.quantity
        )
    }

    return Ingredient(
        id,
        name,
        ingredientMeasurements,
        category,
        packages
    )
}

fun RecipeApi.toDomain(
    measurements: List<Measurement>,
    recipeCategories: List<RecipeCategory>,
    ingredientCategories: List<IngredientCategory>
): Recipe {

    val ingredientGroups = this.ingredientGroups.map { ig ->
        IngredientGroup(
            ig.id,
            ig.name,
            ig.recipeIngredients.map { ri ->
                RecipeIngredient(
                    ri.id,
                    ri.quantity,
                    ri.ingredient.toDomain(measurements, ingredientCategories),
                    measurements.find { measurement -> measurement.id == ri.measurementId }?.let { it }
                        ?: throw NullPointerException("recipe ingredient measurement cannot be null")
                )
            }

        )
    }

    val category =
        recipeCategories.find { cat -> cat.id == categoryId }?.let { it }
            ?: throw NullPointerException("recipe category cannot be null")

    return Recipe(
        id,
        name,
        directions,
        servings,
        ingredientGroups,
        imageUrl,
        category
    )
}

fun AboutApi.toDomain() =
    About(about, backendVersion)

fun HouseApi.toDomain() =
    House(id, name, joinCode)

fun IngredientCategoryApi.toDomain() =
    IngredientCategory(id, name)

fun MeasurementApi.toDomain(): Measurement {
    return Measurement(
        id,
        primary,
        localizations.map { it.toDomain() }
    )
}

fun MeasurementLocalizationApi.toDomain(): MeasurementLocalization {
    return MeasurementLocalization(
        name,
        imperialSuffix,
        metricSuffix,
        imperial1000Suffix,
        metric1000Suffix,
        imperialSuffixPlural,
        metricSuffixPlural,
        imperial1000SuffixPlural,
        metric1000SuffixPlural
    )
}

fun RecipeCategoryApi.toDomain(): RecipeCategory {
    return RecipeCategory(id, name)
}

fun UserApi.toDomain(): User {
    return User(userId, email, name)
}
