package com.astutify.mealplanner.core.model.domain.mapper

import com.astutify.mealplanner.core.model.data.HouseApi
import com.astutify.mealplanner.core.model.data.IngredientApi
import com.astutify.mealplanner.core.model.data.IngredientGroupApi
import com.astutify.mealplanner.core.model.data.IngredientMeasurementApi
import com.astutify.mealplanner.core.model.data.PackageApi
import com.astutify.mealplanner.core.model.data.RecipeApi
import com.astutify.mealplanner.core.model.data.RecipeIngredientApi
import com.astutify.mealplanner.core.model.domain.House
import com.astutify.mealplanner.core.model.domain.Ingredient
import com.astutify.mealplanner.core.model.domain.IngredientGroup
import com.astutify.mealplanner.core.model.domain.IngredientMeasurement
import com.astutify.mealplanner.core.model.domain.IngredientPackage
import com.astutify.mealplanner.core.model.domain.Recipe
import com.astutify.mealplanner.core.model.domain.RecipeIngredient

fun House.toData() = HouseApi(id, name, joinCode)

fun Ingredient.toData() = IngredientApi(
    id,
    name,
    measurements.map { it.toData() },
    category.id,
    packages?.map { it.toData() }
)

fun IngredientGroup.toData() = IngredientGroupApi(
    id,
    name,
    recipeIngredients.map { it.toData() }
)

fun IngredientMeasurement.toData() = IngredientMeasurementApi(
    id,
    quantity,
    measurement.id
)

fun IngredientPackage.toData() =
    PackageApi(id, name, quantity)

fun Recipe.toData() = RecipeApi(
    id,
    name,
    directions,
    servings,
    ingredientGroups.map { it.toData() },
    imageUrl,
    category.id
)

fun RecipeIngredient.toData() = RecipeIngredientApi(
    id,
    quantity,
    ingredient.toData(),
    measurement.id
)
