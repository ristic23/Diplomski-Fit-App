package com.example.jetpackcomposesvastara.di

import android.content.Context
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.jetpackcomposesvastara.data.GoogleFitDataSource
import com.example.jetpackcomposesvastara.data.Repository
import com.example.jetpackcomposesvastara.data.RepositoryInterface
import com.example.jetpackcomposesvastara.presentation.viewModel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@ExperimentalComposeUiApi
object AppModule {


    @Singleton
    @Provides
    fun provideRepository(googleFitDataSource: GoogleFitDataSource): RepositoryInterface =
        Repository(googleFitDataSource)

    @Singleton
    @Provides
    fun provideGoogleFitDataSource(@ApplicationContext activityContext: Context): GoogleFitDataSource =
        GoogleFitDataSource(activityContext)

    @Singleton
    @Provides
    fun provideMainViewModel(repositoryInterface: RepositoryInterface): MainViewModel =
        MainViewModel(repositoryInterface)



}