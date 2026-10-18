package com.equipo.wikiastrolabio.di

import com.equipo.wikiastrolabio.data.repository.ArchivosRepositoryImpl
import com.equipo.wikiastrolabio.data.repository.ContenidoRepositoryImpl
import com.equipo.wikiastrolabio.data.repository.HistoriaRepositoryImpl
import com.equipo.wikiastrolabio.data.repository.MecanicasRepositoryImpl
import com.equipo.wikiastrolabio.data.repository.MundoRepositoryImpl
import com.equipo.wikiastrolabio.data.repository.PersonajesRepositoryImpl
import com.equipo.wikiastrolabio.domain.repository.ArchivosRepository
import com.equipo.wikiastrolabio.domain.repository.ContenidoRepository
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import com.equipo.wikiastrolabio.domain.repository.MecanicasRepository
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import com.equipo.wikiastrolabio.domain.repository.PersonajesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Une cada interfaz del dominio con su implementación de la capa de datos. */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriosModule {

    @Binds
    @Singleton
    abstract fun bindContenidoRepository(impl: ContenidoRepositoryImpl): ContenidoRepository

    @Binds
    @Singleton
    abstract fun bindHistoriaRepository(impl: HistoriaRepositoryImpl): HistoriaRepository

    @Binds
    @Singleton
    abstract fun bindMundoRepository(impl: MundoRepositoryImpl): MundoRepository

    @Binds
    @Singleton
    abstract fun bindPersonajesRepository(impl: PersonajesRepositoryImpl): PersonajesRepository

    @Binds
    @Singleton
    abstract fun bindMecanicasRepository(impl: MecanicasRepositoryImpl): MecanicasRepository

    @Binds
    @Singleton
    abstract fun bindArchivosRepository(impl: ArchivosRepositoryImpl): ArchivosRepository
}
