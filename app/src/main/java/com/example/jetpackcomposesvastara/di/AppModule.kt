package com.example.jetpackcomposesvastara.di

import android.content.Context
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.jetpackcomposesvastara.data.GoogleFitDataSource
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFit
import com.example.jetpackcomposesvastara.data.RepositoryGoogleFitInterface
import com.example.jetpackcomposesvastara.presentation.viewModel.MainViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
@ExperimentalComposeUiApi
object AppModule {


    @Singleton
    @Provides
    fun provideRepositoryGoogleFit(googleFitDataSource: GoogleFitDataSource): RepositoryGoogleFitInterface =
        RepositoryGoogleFit(googleFitDataSource)

    @Singleton
    @Provides
    fun provideGoogleFitDataSource(@ApplicationContext activityContext: Context): GoogleFitDataSource =
        GoogleFitDataSource(activityContext)

    @Singleton
    @Provides
    fun provideMainViewModel(repositoryGoogleFitInterface: RepositoryGoogleFitInterface): MainViewModel =
        MainViewModel(repositoryGoogleFitInterface)



}