package com.equipo.wikiastrolabio.ui.feature.archivos

import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.domain.usecase.archivos.ObtenerCategoriasArchivoUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArchivosViewModel @Inject constructor(
    private val obtenerCategorias: ObtenerCategoriasArchivoUseCase,
) : ContenidoViewModel<List<CategoriaArchivo>>() {

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerCategorias(forzarRed)
}
