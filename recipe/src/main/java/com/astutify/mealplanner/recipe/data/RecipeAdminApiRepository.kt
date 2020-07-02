package com.astutify.mealplanner.recipe.data

import android.net.Uri
import com.astutify.mealplanner.core.entity.data.RecipeEntity
import com.astutify.mealplanner.core.entity.data.error.ApiErrorManager
import com.astutify.mealplanner.coreui.presentation.utils.ImagePickerUtils
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import io.reactivex.Single.error
import io.reactivex.Single.just
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RecipeAdminApiRepository @Inject constructor(
    private val api: RecipeApi,
    private val imagePickerUtils: ImagePickerUtils,
    private val apiErrorManager: ApiErrorManager
) {

    fun saveRecipe(recipe: RecipeEntity) =
        api.saveRecipe(getRecipeBody(recipe), getImageBody(recipe))
            .flatMap {
                if (it.isSuccessful) {
                    just(it.body()!!)
                } else {
                    error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
                }
            }

    fun deleteRecipe(recipeId: String) = api.deleteRecipe(recipeId)
        .flatMap {
            if (it.isSuccessful) {
                just(Unit)
            } else {
                error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
            }
        }

    fun updateRecipe(recipe: RecipeEntity) =
        api.updateRecipe(getRecipeBody(recipe), getImageBody(recipe))
            .flatMap {
                if (it.isSuccessful) {
                    just(it.body()!!)
                } else {
                    error(apiErrorManager.mapError(it.code(), it.errorBody()?.string()))
                }
            }

    private fun getRecipeBody(recipe: RecipeEntity): RequestBody {
        val adapter: JsonAdapter<RecipeEntity> =
            Moshi.Builder().build().adapter(RecipeEntity::class.java)
        val recipeJson = adapter.toJson(recipe)
        return recipeJson!!.toRequestBody(REQUEST_BODY_JSON.toMediaTypeOrNull())
    }

    private fun getImageBody(recipe: RecipeEntity): MultipartBody.Part? {
        return recipe.imageUrl.let { imageUrl ->
            imagePickerUtils.getMultipartImageBody(Uri.parse(imageUrl))
        }
    }

    companion object {
        private const val REQUEST_BODY_JSON = "application/json"
    }
}
