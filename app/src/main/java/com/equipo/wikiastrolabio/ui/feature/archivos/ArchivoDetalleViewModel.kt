package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.domain.usecase.archivos.ObtenerArchivoUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import com.equipo.wikiastrolabio.ui.navigation.Ruta
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ArchivoDetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val obtenerArchivo: ObtenerArchivoUseCase,
) : ContenidoViewModel<Archivo>() {

    private val slug = savedStateHandle.toRoute<Ruta.ArchivoDetalle>().slug

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerArchivo(slug, forzarRed)
}
