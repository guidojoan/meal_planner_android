package com.astutify.mealplanner.coreui.entity.mapper

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
import com.astutify.mealplanner.coreui.entity.AboutViewModel
import com.astutify.mealplanner.coreui.entity.HouseViewModel
import com.astutify.mealplanner.coreui.entity.IngredientCategoryViewModel
import com.astutify.mealplanner.coreui.entity.IngredientGroupViewModel
import com.astutify.mealplanner.coreui.entity.IngredientMeasurementViewModel
import com.astutify.mealplanner.coreui.entity.IngredientPackageViewModel
import com.astutify.mealplanner.coreui.entity.IngredientViewModel
import com.astutify.mealplanner.coreui.entity.MeasurementViewModel
import com.astutify.mealplanner.coreui.entity.RecipeCategoryViewModel
import com.astutify.mealplanner.coreui.entity.RecipeIngredientViewModel
import com.astutify.mealplanner.coreui.entity.RecipeViewModel
import com.astutify.mealplanner.coreui.entity.UserViewModel

fun HouseViewModel.toDomain() = House(id, name, joinCode)

fun IngredientPackageViewModel.toDomain() = IngredientPackage(id, name, quantity)

fun IngredientCategoryViewModel.toDomain() = IngredientCategory(id, name)

fun MeasurementViewModel.toDomain() =
    Measurement(
        id,
        primary,
        listOf(
            MeasurementLocalization(
                name,
                imperialSuffix,
                metricSuffix,
                imperial1000Suffix,
                metric1000Suffix,
                imperial1000SuffixPlural,
                metric1000SuffixPlural,
                imperial1000SuffixPlural
            )
        )
    )

fun IngredientMeasurementViewModel.toDomain(): IngredientMeasurement {
    return IngredientMeasurement(id, quantity, measurement.toDomain())
}

fun IngredientViewModel.toDomain(): Ingredient {
    return Ingredient(
        id,
        name,
        measurements.map { it.toDomain() },
        category.toDomain(),
        packages?.map { it.toDomain() }
    )
}

fun RecipeIngredientViewModel.toDomain() =
    RecipeIngredient(id, quantity, ingredient.toDomain(), measurement.toDomain())

fun IngredientGroupViewModel.toDomain() =
    IngredientGroup(id, name, recipeIngredients.map { it.toDomain() })

fun RecipeCategoryViewModel.toDomain() = RecipeCategory(id, name)

fun RecipeViewModel.toDomain(): Recipe {
    return Recipe(
        id,
        name,
        directions,
        servings,
        ingredientGroups.map { it.toDomain() },
        imageUrl,
        recipeCategory.toDomain()
    )
}

fun User.toPresentation() = UserViewModel(userId, email, name)

fun House.toPresentation() = HouseViewModel(id, name, joinCode)

fun Measurement.toPresentation() =
    MeasurementViewModel(
        id, localizations[0].name, primary,
        localizations[0].imperialSuffix,
        localizations[0].metricSuffix,
        localizations[0].imperial1000Suffix,
        localizations[0].metric1000Suffix,
        localizations[0].imperialSuffixPlural,
        localizations[0].metricSuffixPlural,
        localizations[0].imperial1000SuffixPlural,
        localizations[0].metric1000SuffixPlural
    )

fun RecipeCategory.toPresentation() =
    RecipeCategoryViewModel(id, name)

fun Ingredient.toPresentation(): IngredientViewModel {
    return IngredientViewModel(
        id,
        name,
        measurements.map { it.toPresentation() },
        category.toPresentation(),
        packages?.map { it.toPresentation() }
    )
}

fun IngredientCategory.toPresentation() =
    IngredientCategoryViewModel(id, name)

fun About.toPresentation() = AboutViewModel(about, backendVersion)

fun Recipe.toPresentation() = RecipeViewModel(
    id,
    name,
    directions,
    servings,
    ingredientGroups.map { it.toPresentation() },
    imageUrl,
    category.toPresentation()
)

fun IngredientMeasurement.toPresentation() =
    IngredientMeasurementViewModel(
        id,
        quantity,
        measurement.toPresentation()
    )

fun IngredientPackage.toPresentation() = IngredientPackageViewModel(
    id,
    name,
    quantity
)

fun IngredientGroup.toPresentation() =
    IngredientGroupViewModel(
        id,
        name,
        recipeIngredients.map { it.toPresentation() }
    )

fun RecipeIngredient.toPresentation() =
    RecipeIngredientViewModel(
        id,
        quantity,
        ingredient.toPresentation(),
        measurement.toPresentation()
    )
