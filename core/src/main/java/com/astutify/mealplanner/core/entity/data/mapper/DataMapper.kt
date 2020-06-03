package com.astutify.mealplanner.core.entity.data.mapper

import com.astutify.mealplanner.core.entity.data.AboutEntity
import com.astutify.mealplanner.core.entity.data.HouseEntity
import com.astutify.mealplanner.core.entity.data.IngredientCategoryEntity
import com.astutify.mealplanner.core.entity.data.IngredientEntity
import com.astutify.mealplanner.core.entity.data.MeasurementEntity
import com.astutify.mealplanner.core.entity.data.MeasurementLocalizationEntity
import com.astutify.mealplanner.core.entity.data.RecipeCategoryEntity
import com.astutify.mealplanner.core.entity.data.RecipeEntity
import com.astutify.mealplanner.core.entity.data.UserEntity
import com.astutify.mealplanner.core.entity.domain.About
import com.astutify.mealplanner.core.entity.domain.House
import com.astutify.mealplanner.core.entity.domain.Ingredient
import com.astutify.mealplanner.core.entity.domain.IngredientCategory
import com.astutify.mealplanner.core.entity.domain.IngredientGroup
import com.astutify.mealplanner.core.entity.domain.IngredientMeasurement
import com.astutify.mealplanner.core.entity.domain.IngredientPackage
import com.astutify.mealplanner.core.entity.domain.Measurement
import com.astutify.mealplanner.core.entity.domain.MeasurementLocalization
import com.astutify.mealplanner.core.entity.domain.Recipe
import com.astutify.mealplanner.core.entity.domain.RecipeCategory
import com.astutify.mealplanner.core.entity.domain.RecipeIngredient
import com.astutify.mealplanner.core.entity.domain.User
import java.lang.NullPointerException

fun IngredientEntity.toDomain(
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

fun RecipeEntity.toDomain(
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

fun AboutEntity.toDomain() =
    About(about, backendVersion)

fun HouseEntity.toDomain() =
    House(id, name, joinCode)

fun IngredientCategoryEntity.toDomain() =
    IngredientCategory(id, name)

fun MeasurementEntity.toDomain(): Measurement {
    return Measurement(
        id,
        primary,
        localizations.map { it.toDomain() }
    )
}

fun MeasurementLocalizationEntity.toDomain(): MeasurementLocalization {
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

fun RecipeCategoryEntity.toDomain(): RecipeCategory {
    return RecipeCategory(id, name)
}

fun UserEntity.toDomain(): User {
    return User(userId, email, name)
}
