package com.equipo.wikiastrolabio.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.equipo.wikiastrolabio.data.local.AstrolabioDatabase
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BaseDatosModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AstrolabioDatabase =
        Room.databaseBuilder(context, AstrolabioDatabase::class.java, AstrolabioDatabase.NOMBRE)
            // Solo es caché: al cambiar de versión se borra y se vuelve a descargar.
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()

    @Provides
    fun provideCacheRespuestaDao(database: AstrolabioDatabase): CacheRespuestaDao =
        database.cacheRespuestaDao()

    @Provides
    @Singleton
    fun providePreferencias(@ApplicationContext context: Context): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile("preferencias") },
        )
}
