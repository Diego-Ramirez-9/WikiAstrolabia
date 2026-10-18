package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import javax.inject.Inject

class HistoriaRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
) : HistoriaRepository {

    override suspend fun obtenerHistoria(forzarRed: Boolean): Resultado<List<SeccionHistoria>> =
        cache.obtener("historia", forzarRed) { api.historia().data }
            .map { secciones -> secciones.map { it.aDominio() } }

    override suspend fun obtenerEntrada(slug: String, forzarRed: Boolean): Resultado<EntradaHistoria> =
        cache.obtener("historia/$slug", forzarRed) { api.entradaHistoria(slug).data }
            .map { it.aDominio() }
}
