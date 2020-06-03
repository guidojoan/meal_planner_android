package com.astutify.mealplanner.userprofile

import com.astutify.mealplanner.userprofile.data.HouseApi
import com.astutify.mealplanner.userprofile.data.HouseApiRepository
import com.astutify.mealplanner.userprofile.data.HouseDataRepository
import com.astutify.mealplanner.userprofile.domain.HouseRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UserProfileModule {

    @Provides
    fun providesHouseRepository(
        api: HouseApiRepository
    ): HouseRepository {
        return HouseDataRepository(api)
    }

    @Provides
    fun providesHouseApi(retrofit: Retrofit): HouseApi {
        return retrofit.create(HouseApi::class.java)
    }
}
