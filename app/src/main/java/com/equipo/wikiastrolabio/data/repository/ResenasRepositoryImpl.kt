package com.equipo.wikiastrolabio.data.repository

import com.equipo.wikiastrolabio.data.local.preferencias.PreferenciasResenas
import com.equipo.wikiastrolabio.data.mapper.aDominio
import com.equipo.wikiastrolabio.data.remote.AstrolabioApi
import com.equipo.wikiastrolabio.data.remote.aErrorDatos
import com.equipo.wikiastrolabio.data.remote.dto.NuevaResenaDto
import com.equipo.wikiastrolabio.di.DispatcherIo
import com.equipo.wikiastrolabio.domain.model.NuevaResena
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import com.equipo.wikiastrolabio.domain.model.map
import com.equipo.wikiastrolabio.domain.repository.ResenasRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class ResenasRepositoryImpl @Inject constructor(
    private val api: AstrolabioApi,
    private val cache: CacheDeContenido,
    private val preferencias: PreferenciasResenas,
    private val json: Json,
    @param:DispatcherIo private val dispatcherIo: CoroutineDispatcher,
) : ResenasRepository {

    // forzarRed = true: siempre se descarga; la copia de Room solo sirve sin conexión.
    override suspend fun obtenerResenas(): Resultado<ResumenResenas> =
        cache.obtener("resenas", forzarRed = true) { api.resenas().data }
            .map { it.aDominio() }

    override suspend fun publicar(resena: NuevaResena): Resultado<Resena> = withContext(dispatcherIo) {
        try {
            val dto = NuevaResenaDto(resena.apodo.trim(), resena.calificacion, resena.texto.trim())
            val publicada = api.publicarResena(dto).data.aDominio()
            preferencias.guardarApodo(publicada.apodo)
            Resultado.Exito(publicada, OrigenDatos.RED)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Resultado.Error(e.aErrorDatos(json))
        }
    }

    override suspend fun apodoGuardado(): String? = preferencias.apodo()
}
