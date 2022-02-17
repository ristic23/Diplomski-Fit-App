package com.example.repository.di

import com.example.datastore.DataStoreManager
import com.example.repository.Repository
import com.example.repository.RepositoryInterface
import com.example.roomdb.RoomDBImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule
{
    @Singleton
    @Provides
    fun provideRepository(
        roomDbImpl: RoomDBImpl,
        dataStoreManager: DataStoreManager
    ): RepositoryInterface =
        Repository(roomDbImpl, dataStoreManager)
}