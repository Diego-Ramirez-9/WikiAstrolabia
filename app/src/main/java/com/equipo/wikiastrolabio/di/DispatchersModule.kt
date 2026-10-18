package com.equipo.wikiastrolabio.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier

/** Dispatcher para trabajo de E/S (disco, red, parseo de JSON). En las pruebas se reemplaza. */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DispatcherIo

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {

    @Provides
    @DispatcherIo
    fun provideDispatcherIo(): CoroutineDispatcher = Dispatchers.IO
}
