package com.equipo.wikiastrolabio.domain.repository

import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.Resultado

/*
 * Contrato de la capa de datos: la UI y los casos de uso solo conocen esta interfaz y no saben
 * si el dato viene de la API o de la caché de Room.
 * forzarRed = true ignora la caché vigente (ej. al recargar); si la red falla, se devuelve lo guardado.
 */
interface PersonajesRepository {
    suspend fun obtenerPersonajes(forzarRed: Boolean = false): Resultado<List<PersonajeResumen>>
    suspend fun obtenerPersonaje(slug: String, forzarRed: Boolean = false): Resultado<Personaje>
}
