package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.MecanicasRepository
import javax.inject.Inject

class MecanicasRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
) : MecanicasRepository {

    override suspend fun obtenerMecanicas(forzarRed: Boolean): Resultado<Mecanicas> =
        cache.obtener("mecanicas", forzarRed) { api.mecanicas().data }
            .map { it.aDominio() }
}
