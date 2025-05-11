package com.ilya.data.di

import com.ilya.data.MistakesRepository
import com.ilya.data.WordsRepository
import com.ilya.data.repositiory.MistakesRepositoryImpl
import com.ilya.data.repositiory.WordsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal interface RepositoryModule {

  @Binds
  fun bindMistakesRepository(impl: MistakesRepositoryImpl): MistakesRepository

  @Binds
  fun bindWordRepository(impl: WordsRepositoryImpl): WordsRepository

}