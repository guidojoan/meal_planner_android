package com.astutify.mealplanner.core

import android.content.Context
import android.content.SharedPreferences
import com.astutify.mealplanner.core.authentication.MyAuthenticator
import com.astutify.mealplanner.core.authentication.MyInterceptor
import com.astutify.mealplanner.core.authentication.SessionApi
import com.astutify.mealplanner.core.authentication.SessionManager
import com.astutify.mealplanner.core.data.CategoriesApi
import com.astutify.mealplanner.core.data.CategoriesApiRepository
import com.astutify.mealplanner.core.data.CategoriesDataRepository
import com.astutify.mealplanner.core.data.MeasurementApi
import com.astutify.mealplanner.core.data.MeasurementApiRepository
import com.astutify.mealplanner.core.data.MeasurementDataRepository
import com.astutify.mealplanner.core.domain.CategoriesLocalRepository
import com.astutify.mealplanner.core.domain.CategoriesRepository
import com.astutify.mealplanner.core.domain.MeasurementLocalRepository
import com.astutify.mealplanner.core.domain.MeasurementRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class CoreModule {

    @Provides
    fun providesSharePreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences("NAME", Context.MODE_PRIVATE)
    }

    @Provides
    fun providesOkHttp(authenticator: MyAuthenticator, interceptor: MyInterceptor): OkHttpClient {
        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .authenticator(authenticator)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }
        return builder.build()
    }

    @Provides
    @Named("No_Auth")
    fun providesOkHttpNoAuth(): OkHttpClient {

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.apply { loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY }

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {

        val moshiBuilder = Moshi.Builder()
            .add(Date::class.java, Rfc3339DateJsonAdapter())

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER)
            .addConverterFactory(MoshiConverterFactory.create(moshiBuilder.build()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    }

    @Provides
    @Named("No_Auth")
    fun retrofitNoAuth(retrofitBuilder: Retrofit.Builder, @Named("No_Auth") okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun retrofit(retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun providesSessionManager(sharedPreferences: SharedPreferences): SessionManager {
        return SessionManager(sharedPreferences)
    }

    @Provides
    @Named("executor_thread")
    fun provideExecutorThread(): Scheduler {
        return Schedulers.newThread()
    }

    @Provides
    @Named("ui_thread")
    fun provideUiThread(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    @Provides
    fun providesRefresh(@Named("No_Auth") retrofit: Retrofit): SessionApi {
        return retrofit.create(SessionApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMeasurementLocalRepository() = MeasurementLocalRepository()

    @Provides
    @Singleton
    fun providesCategoriesLocalRepository() = CategoriesLocalRepository()

    @Provides
    fun providesMeasurementApi(retrofit: Retrofit): MeasurementApi {
        return retrofit.create(MeasurementApi::class.java)
    }

    @Provides
    fun providesCategoriesApi(retrofit: Retrofit): CategoriesApi {
        return retrofit.create(CategoriesApi::class.java)
    }

    @Provides
    fun providesMeasurementRepository(
        api: MeasurementApiRepository,
        localRepository: MeasurementLocalRepository
    ): MeasurementRepository {
        return MeasurementDataRepository(api, localRepository)
    }

    @Provides
    fun providesCategoriesRepository(
        api: CategoriesApiRepository,
        localRepository: CategoriesLocalRepository
    ): CategoriesRepository {
        return CategoriesDataRepository(api, localRepository)
    }
}
