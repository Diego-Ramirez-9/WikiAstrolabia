package com.equipo.wikiastrolabio.domain.usecase.historia

import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.HistoriaRepository
import javax.inject.Inject

/** Detalle de una entrada de la Historia. */
class ObtenerEntradaHistoriaUseCase @Inject constructor(
    private val repositorio: HistoriaRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<EntradaHistoria> =
        repositorio.obtenerEntrada(slug, forzarRed)
}
