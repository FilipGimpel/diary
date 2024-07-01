package com.gimpel.diary.di

import com.gimpel.diary.data.DefaultDiaryRepository
import com.gimpel.diary.data.DiaryRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultDiaryRepository): DiaryRepository
}