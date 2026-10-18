package com.equipo.wikiastrolabio.domain.usecase.personajes

import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.PersonajesRepository
import javax.inject.Inject

/** Lista de la pantalla Personajes. */
class ObtenerPersonajesUseCase @Inject constructor(
    private val repositorio: PersonajesRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<List<PersonajeResumen>> =
        repositorio.obtenerPersonajes(forzarRed)
}
