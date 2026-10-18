package com.equipo.wikiastrolabio.domain.repository

import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen

/*
 * Contrato de la capa de datos: la UI y los casos de uso solo conocen esta interfaz y no saben
 * si el dato viene de la API, de la caché de Room o de las respuestas falsas.
 * forzarRed = true ignora la caché vigente (ej. al recargar); si la red falla, se devuelve lo guardado.
 */
interface MundoRepository {
    suspend fun obtenerMapa(forzarRed: Boolean = false): Resultado<Mapa>

    /** Sin [tipo] devuelve todas las zonas; con [TipoZona.CASA], solo las Casas (pestañas). */
    suspend fun obtenerZonas(tipo: TipoZona? = null, forzarRed: Boolean = false): Resultado<List<ZonaResumen>>
    suspend fun obtenerZona(slug: String, forzarRed: Boolean = false): Resultado<Zona>
    suspend fun obtenerSala(slug: String, forzarRed: Boolean = false): Resultado<Sala>
}
