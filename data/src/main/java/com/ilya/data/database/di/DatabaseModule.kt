package com.ilya.data.database.di

import android.content.Context
import androidx.room.Room
import com.ilya.data.database.RussianAccentsApplicationDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): RussianAccentsApplicationDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = RussianAccentsApplicationDatabase::class.java,
            name = "russianAccentsApplicaiton.db"
        ).build()
    }

}