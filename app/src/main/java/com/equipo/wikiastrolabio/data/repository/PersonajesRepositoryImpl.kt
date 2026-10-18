package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.PersonajesRepository
import javax.inject.Inject

class PersonajesRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
) : PersonajesRepository {

    override suspend fun obtenerPersonajes(forzarRed: Boolean): Resultado<List<PersonajeResumen>> =
        cache.obtener("personajes", forzarRed) { api.personajes().data }
            .map { personajes -> personajes.map { it.aDominio() } }

    override suspend fun obtenerPersonaje(slug: String, forzarRed: Boolean): Resultado<Personaje> =
        cache.obtener("personajes/$slug", forzarRed) { api.personaje(slug).data }
            .map { it.aDominio() }
}
