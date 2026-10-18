package com.equipo.wikiastrolabio.domain.usecase.mecanicas

import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.MecanicasRepository
import javax.inject.Inject

/** Habilidades y reglas de la pantalla Mecánicas. */
class ObtenerMecanicasUseCase @Inject constructor(
    private val repositorio: MecanicasRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<Mecanicas> =
        repositorio.obtenerMecanicas(forzarRed)
}
