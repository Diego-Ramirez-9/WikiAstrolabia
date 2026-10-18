package com.equipo.wikiastrolabio.domain.usecase.personajes

import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.PersonajesRepository
import javax.inject.Inject

/** Ficha completa de un personaje ("Ver más"). */
class ObtenerPersonajeUseCase @Inject constructor(
    private val repositorio: PersonajesRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<Personaje> =
        repositorio.obtenerPersonaje(slug, forzarRed)
}
