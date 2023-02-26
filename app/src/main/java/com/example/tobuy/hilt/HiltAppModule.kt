package com.example.tobuy.hilt

import com.example.tobuy.arch.ToButRepo
import com.example.tobuy.room.appDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HiltAppModule {
    @Provides
    @Singleton
    fun provideToBuyRepo() = ToButRepo(appDatabase)
}