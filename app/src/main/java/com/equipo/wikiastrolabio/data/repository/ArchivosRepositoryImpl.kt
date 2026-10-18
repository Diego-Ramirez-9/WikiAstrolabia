package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.ArchivosRepository
import javax.inject.Inject

class ArchivosRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
) : ArchivosRepository {

    override suspend fun obtenerCategorias(forzarRed: Boolean): Resultado<List<CategoriaArchivo>> =
        cache.obtener("archivos", forzarRed) { api.archivos().data }
            .map { categorias -> categorias.map { it.aDominio() } }

    override suspend fun obtenerArchivo(slug: String, forzarRed: Boolean): Resultado<Archivo> =
        cache.obtener("archivos/$slug", forzarRed) { api.archivo(slug).data }
            .map { it.aDominio() }
}
