package com.astutify.mealplanner.recipe.data

import com.astutify.mealplanner.core.model.data.RecipeApi
import com.astutify.mealplanner.core.model.data.error.ApiErrorManager
import io.reactivex.Single
import io.reactivex.Single.error
import io.reactivex.Single.just
import javax.inject.Inject

class RecipesApiRepository @Inject constructor(
    private val api: RecipesApi,
    private val apiErrorManager: ApiErrorManager
) {

    private var page = 0
    private val pageSize = PAGE_SIZE
    private var nextPage: Int? = null
    private var keywords: String? = null

    fun firstPage(keywords: String?): Single<List<RecipeApi>> {
        initPaging()
        this.keywords = keywords
        return api.getRecipes(keywords, page, pageSize)
            .flatMap { response ->
                if (response.isSuccessful) {
                    val result = response.body()!!
                    nextPage = result.paging.next
                    just(result.results)
                } else {
                    error(apiErrorManager.mapError(response.code(), response.errorBody()?.string()))
                }
            }
    }

    fun nextPage() =
        nextPage?.let { page ->
            api.getRecipes(keywords, page, pageSize)
                .flatMap { response ->
                    if (response.isSuccessful) {
                        val result = response.body()!!
                        nextPage = result.paging.next
                        just(result.results)
                    } else {
                        error(
                            apiErrorManager.mapError(
                                response.code(),
                                response.errorBody()?.string()
                            )
                        )
                    }
                }
        } ?: just(emptyList())

    private fun initPaging() {
        this.page = 0
        this.nextPage = null
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}
