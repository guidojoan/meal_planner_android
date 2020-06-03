package com.astutify.mealplanner.core

import android.content.Context
import android.content.SharedPreferences
import com.astutify.mealplanner.core.authentication.SessionApi
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.core.domain.CategoriesRepository
import com.astutify.mealplanner.core.domain.MeasurementRepository
import dagger.BindsInstance
import dagger.Component
import io.reactivex.Scheduler
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [CoreModule::class])
interface CoreComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun withContext(context: Context): Builder

        fun build(): CoreComponent
    }

    fun providesSharedPreferences(): SharedPreferences

    fun providesOkHttp(): OkHttpClient

    @Named("No_Auth")
    fun providesOkHttpNoAuth(): OkHttpClient

    fun providesRetrofitBuilder(): Retrofit.Builder

    @Named("No_Auth")
    fun retrofitNoAuth(): Retrofit

    fun retrofit(): Retrofit

    fun providesSessionManager(): SessionManager

    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler

    @Named("ui_thread")
    fun provideUiThread(): Scheduler

    fun providesAuthApi(): SessionApi

    fun providesContext(): Context

    fun providesMeasurementRepository(): MeasurementRepository

    fun providesCategoriesRepository(): CategoriesRepository

    fun providesCategoriesDataRepository(): CategoriesDataRepository

    fun providesMeasurementsDataRepository(): MeasurementDataRepository
}
