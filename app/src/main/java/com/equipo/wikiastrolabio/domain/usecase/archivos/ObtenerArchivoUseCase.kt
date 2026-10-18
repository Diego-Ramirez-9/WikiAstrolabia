package com.equipo.wikiastrolabio.domain.usecase.archivos

import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.ArchivosRepository
import javax.inject.Inject

/** Detalle de un poema o nota. */
class ObtenerArchivoUseCase @Inject constructor(
    private val repositorio: ArchivosRepository,
) {
    suspend operator fun invoke(slug: String, forzarRed: Boolean = false): Resultado<Archivo> =
        repositorio.obtenerArchivo(slug, forzarRed)
}
