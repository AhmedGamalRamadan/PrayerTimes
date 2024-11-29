package com.ag.projects.aatask.di

import com.ag.projects.aatask.util.Constants
import com.ag.projects.data.remote.PrayerTimesService
import com.ag.projects.data.respoitory.PrayerTimeRepository
import com.ag.projects.data.respoitory.PrayerTimeRepositoryImpl
import com.ag.projects.domain.usecase.prayer_times.GetPrayerTimesUseCase
import com.ag.projects.domain.usecase.qibla.GetQiblaDirectionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MainModule {


    @Provides
    @Singleton
    fun providePrayerTimesAPI(): PrayerTimesService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimesService::class.java)
    }

    @Provides
    @Singleton
    fun providePrayerTimesRepository(
        prayerTimesService: PrayerTimesService
    ): PrayerTimeRepository {
        return PrayerTimeRepositoryImpl(prayerTimesService)
    }

    @Provides
    @Singleton
    fun providePrayerTimesUseCase(prayerTimeRepository: PrayerTimeRepository) =
        GetPrayerTimesUseCase(prayerTimeRepository)


    @Provides
    @Singleton
    fun provideQiblaUseCase(prayerTimeRepository: PrayerTimeRepository) =
        GetQiblaDirectionUseCase(prayerTimeRepository)


}