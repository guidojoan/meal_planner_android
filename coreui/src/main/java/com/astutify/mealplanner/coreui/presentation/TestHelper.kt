package com.astutify.mealplanner.coreui.presentation

import com.astutify.mealplanner.core.entity.data.error.BadRequest
import com.astutify.mealplanner.core.entity.data.error.ErrorItem
import com.astutify.mealplanner.core.entity.data.error.ErrorType
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

class TestHelper {

    companion object {
        fun getPrimaryMeasurementVM() =
            MeasurementViewModel(
                id = "measurementId",
                name = "Volume",
                primary = true,
                imperialSuffix = "fl oz",
                metricSuffix = "ml"
            )

        fun getPrimaryMeasurement() =
            Measurement(
                id = "measurementId",
                primary = true,
                localizations = listOf(getMeasurementTranslation())
            )

        fun getMeasurementTranslation() =
            MeasurementLocalization(
                name = "Volume",
                imperialSuffix = "fl oz",
                metricSuffix = "ml"
            )

        fun getCustomMeasurementTranslation() =
            MeasurementLocalization(
                name = "Cup",
                imperialSuffix = "Cup.",
                metricSuffix = "Cup."
            )

        fun getCustomMeasurementVM() =
            MeasurementViewModel(
                id = "customMeasurementId",
                name = "Cup",
                primary = false,
                imperialSuffix = "Cup.",
                metricSuffix = "Cup."
            )

        fun getCustomMeasurement() =
            Measurement(
                id = "customMeasurementId",
                primary = false,
                localizations = listOf(getCustomMeasurementTranslation())
            )

        fun getIngredientCategoryVM() =
            IngredientCategoryViewModel(
                id = "ingredientCategoryId",
                name = "Frozen Food"
            )

        fun getIngredientVM() =
            IngredientViewModel(
                id = "ingredientId",
                name = "Tomato",
                measurements = listOf(getIngredientMeasurementVM()),
                category = getIngredientCategoryVM(),
                packages = listOf(
                    IngredientPackageViewModel(
                        "packageId",
                        "Brick",
                        500f
                    )
                ),
                status = IngredientViewModel.Status.SEEN
            )

        fun getIngredientMeasurementVM() =
            IngredientMeasurementViewModel(
                id = "ingredientMeasurementId",
                quantity = 0f,
                measurement = getPrimaryMeasurementVM()
            )

        fun getIngredient() =
            Ingredient(
                id = "ingredientId",
                name = "Tomato",
                measurements = listOf(getIngredientMeasurement()),
                category = getIngredientCategory(),
                packages = listOf(IngredientPackage("packageId", "Brick", 500f))
            )

        fun getIngredientMeasurement() =
            IngredientMeasurement(
                id = "ingredientMeasurementId",
                quantity = 0f,
                measurement = getPrimaryMeasurement()
            )

        fun getIngredientCategory() =
            IngredientCategory(
                id = "ingredientCategoryId",
                name = "Frozen Food"
            )

        fun getRecipe() =
            Recipe(
                id = "recipeId",
                name = "Pizza",
                directions = "How to make Pizze",
                servings = 4,
                ingredientGroups = listOf(getIngredientGroup()),
                imageUrl = "imageUrl",
                category = getRecipeCategory()
            )

        fun getRecipeCategory() =
            RecipeCategory(
                id = "recipeCategoryId",
                name = "Pastas"
            )

        fun getIngredientGroup() =
            IngredientGroup(
                id = "ingredientGroupId",
                name = "Masa",
                recipeIngredients = listOf(getRecipeIngredient())
            )

        fun getRecipeIngredient() =
            RecipeIngredient(
                id = "recipeIngredientId",
                quantity = 250f,
                ingredient = getIngredient(),
                measurement = getPrimaryMeasurement()
            )

        fun getRecipeVM() =
            RecipeViewModel(
                id = "recipeId",
                name = "Pizza",
                directions = "How to make Pizze",
                servings = 4,
                ingredientGroups = listOf(getIngredientGroupVM()),
                imageUrl = "imageUrl",
                recipeCategory = getRecipeCategoryVM()
            )

        fun getRecipeCategoryVM() =
            RecipeCategoryViewModel(
                id = "recipeCategoryId",
                name = "Pastas"
            )

        fun getIngredientGroupVM() =
            IngredientGroupViewModel(
                id = "ingredientGroupId",
                name = "Masa",
                recipeIngredients = listOf(getRecipeIngredientVM())
            )

        fun getRecipeIngredientVM() =
            RecipeIngredientViewModel(
                id = "recipeIngredientId",
                quantity = 250f,
                ingredient = getIngredientVM(),
                measurement = getPrimaryMeasurementVM()
            )

        fun getNameTakenError() =
            BadRequest(listOf(ErrorItem(ErrorType.VALUE_TAKEN, "name")))

        fun getUser() =
            User("userId", "user@gmail.com", "userName")

        fun getUserVM() =
            UserViewModel("userId", "user@gmail.com", "userName")

        fun getHouse() =
            House("houseId", "houseName", 0)

        fun getHouseVM() =
            HouseViewModel("houseId", "houseName", 0)

        fun getAbout() =
            About("about", "1.2.0")

        fun getAboutVM() =
            AboutViewModel("about", "1.2.0")

        fun getPackageVM() =
            IngredientPackageViewModel("packageId", "packageName", 80f)

        fun getPackage() =
            IngredientPackage("packageId", "packageName", 80f)
    }
}
