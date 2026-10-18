package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.mapper.valorApi
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.MundoRepository
import javax.inject.Inject

class MundoRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
) : MundoRepository {

    override suspend fun obtenerMapa(forzarRed: Boolean): Resultado<Mapa> =
        cache.obtener("mapa", forzarRed) { api.mapa().data }
            .map { it.aDominio() }

    override suspend fun obtenerZonas(tipo: TipoZona?, forzarRed: Boolean): Resultado<List<ZonaResumen>> {
        val filtro = tipo?.valorApi
        val clave = if (filtro == null) "zonas" else "zonas?tipo=$filtro"
        return cache.obtener(clave, forzarRed) { api.zonas(filtro).data }
            .map { zonas -> zonas.map { it.aDominio() } }
    }

    override suspend fun obtenerZona(slug: String, forzarRed: Boolean): Resultado<Zona> =
        cache.obtener("zonas/$slug", forzarRed) { api.zona(slug).data }
            .map { it.aDominio() }

    override suspend fun obtenerSala(slug: String, forzarRed: Boolean): Resultado<Sala> =
        cache.obtener("salas/$slug", forzarRed) { api.sala(slug).data }
            .map { it.aDominio() }
}
