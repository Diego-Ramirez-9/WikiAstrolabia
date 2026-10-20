package com.equipo.wikiastrolabio.domain.repository

import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria

/*
 * Contrato de la capa de datos: la UI y los casos de uso solo conocen esta interfaz y no saben
 * si el dato viene de la API o de la caché de Room.
 * forzarRed = true ignora la caché vigente (ej. al recargar); si la red falla, se devuelve lo guardado.
 */
interface HistoriaRepository {
    suspend fun obtenerHistoria(forzarRed: Boolean = false): Resultado<List<SeccionHistoria>>
    suspend fun obtenerEntrada(slug: String, forzarRed: Boolean = false): Resultado<EntradaHistoria>
}
