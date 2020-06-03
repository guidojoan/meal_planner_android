package com.astutify.mealplanner.core.entity.domain.mapper

import com.astutify.mealplanner.core.entity.data.HouseEntity
import com.astutify.mealplanner.core.entity.data.IngredientEntity
import com.astutify.mealplanner.core.entity.data.IngredientGroupEntity
import com.astutify.mealplanner.core.entity.data.IngredientMeasurementEntity
import com.astutify.mealplanner.core.entity.data.PackageEntity
import com.astutify.mealplanner.core.entity.data.RecipeEntity
import com.astutify.mealplanner.core.entity.data.RecipeIngredientEntity
import com.astutify.mealplanner.core.entity.domain.House
import com.astutify.mealplanner.core.entity.domain.Ingredient
import com.astutify.mealplanner.core.entity.domain.IngredientGroup
import com.astutify.mealplanner.core.entity.domain.IngredientMeasurement
import com.astutify.mealplanner.core.entity.domain.IngredientPackage
import com.astutify.mealplanner.core.entity.domain.Recipe
import com.astutify.mealplanner.core.entity.domain.RecipeIngredient

fun House.toData() = HouseEntity(id, name, joinCode)

fun Ingredient.toData() = IngredientEntity(
    id,
    name,
    measurements.map { it.toData() },
    category.id,
    packages?.map { it.toData() }
)

fun IngredientGroup.toData() = IngredientGroupEntity(
    id,
    name,
    recipeIngredients.map { it.toData() }
)

fun IngredientMeasurement.toData() = IngredientMeasurementEntity(
    id,
    quantity,
    measurement.id
)

fun IngredientPackage.toData() =
    PackageEntity(id, name, quantity)

fun Recipe.toData() = RecipeEntity(
    id,
    name,
    directions,
    servings,
    ingredientGroups.map { it.toData() },
    imageUrl,
    category.id
)

fun RecipeIngredient.toData() = RecipeIngredientEntity(
    id,
    quantity,
    ingredient.toData(),
    measurement.id
)
