package com.equipo.wikiastrolabio.domain.usecase.archivos

import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.repository.ArchivosRepository
import javax.inject.Inject

/** Categorías (acordeones) de la pantalla Archivos con sus elementos. */
class ObtenerCategoriasArchivoUseCase @Inject constructor(
    private val repositorio: ArchivosRepository,
) {
    suspend operator fun invoke(forzarRed: Boolean = false): Resultado<List<CategoriaArchivo>> =
        repositorio.obtenerCategorias(forzarRed)
}
