package com.ag.projects.aatask.di

import android.app.Application
import androidx.room.Room
import com.ag.projects.aatask.util.Constants
import com.ag.projects.aatask.util.network.NetworkConnection
import com.ag.projects.data.local.PrayerTimesDataBase
import com.ag.projects.data.remote.PrayerTimesService
import com.ag.projects.data.respoitory.remote.PrayerTimeRepositoryImpl
import com.ag.projects.data.respoitory.local.PrayerTimesLocalRepositoryImpl
import com.ag.projects.domain.repository.remote.PrayerTimeRepository
import com.ag.projects.domain.repository.local.PrayerTimesLocalRepository
import com.ag.projects.domain.usecase.local.ClearPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.GetLocalPrayerTimesUseCase
import com.ag.projects.domain.usecase.local.InsertPrayerTimesUseCase
import com.ag.projects.domain.usecase.prayer_times.GetRemotePrayerTimesUseCase
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
        GetRemotePrayerTimesUseCase(prayerTimeRepository)


    @Provides
    @Singleton
    fun provideQiblaUseCase(prayerTimeRepository: PrayerTimeRepository) =
        GetQiblaDirectionUseCase(prayerTimeRepository)


    @Provides
    @Singleton
    fun provideDatabase(app: Application): PrayerTimesDataBase {
        return Room.databaseBuilder(
            app,
            PrayerTimesDataBase::class.java,
            "prayer_times.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideLocalRepository(dataBase: PrayerTimesDataBase): PrayerTimesLocalRepository {
        return PrayerTimesLocalRepositoryImpl(dataBase)
    }

    @Provides
    @Singleton
    fun provideGetPrayerTimesLocalUseCase(
        prayerTimesLocalRepository: PrayerTimesLocalRepository
    ): GetLocalPrayerTimesUseCase =
        GetLocalPrayerTimesUseCase(prayerTimesLocalRepository)

    @Provides
    @Singleton
    fun provideInsertPrayerTimesUseCase(
        prayerTimesLocalRepository: PrayerTimesLocalRepository
    ): InsertPrayerTimesUseCase =
        InsertPrayerTimesUseCase(prayerTimesLocalRepository)

    @Provides
    @Singleton
    fun provideClearPrayerTimesUseCase(
        prayerTimesLocalRepository: PrayerTimesLocalRepository
    ): ClearPrayerTimesUseCase =
        ClearPrayerTimesUseCase(prayerTimesLocalRepository)

    @Provides
    @Singleton
    fun provideNetworkContext(context: Application): NetworkConnection = NetworkConnection(context)
}