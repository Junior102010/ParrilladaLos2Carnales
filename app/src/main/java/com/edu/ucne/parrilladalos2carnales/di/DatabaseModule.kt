package com.edu.ucne.parrilladalos2carnales.di

import android.content.Context
import androidx.room.Room
import com.edu.ucne.parrilladalos2carnales.database.ParrilladaDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideParrilladaDb(@ApplicationContext context: Context): ParrilladaDb {
        return Room.databaseBuilder(
            context,
            ParrilladaDb::class.java,
            "ParrilladaDb.db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePlatoDao(parrilladaDb: ParrilladaDb) = parrilladaDb.platoDao()
}