package com.equipo.wikiastrolabio.domain.usecase.historia

import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import javax.inject.Inject

/** Secciones y entradas de la pantalla Historia. */
class ObtenerHistoriaUseCase @Inject constructor(
    private val repositorio: HistoriaRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<List<SeccionHistoria>> =
        repositorio.obtenerHistoria(forzarRed)
}
