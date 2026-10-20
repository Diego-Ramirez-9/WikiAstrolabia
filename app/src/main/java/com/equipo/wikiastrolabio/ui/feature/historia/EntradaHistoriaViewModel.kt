package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.equipo.wikiastrolabio.domain.model.LecturaHistoria
import com.equipo.wikiastrolabio.domain.usecase.historia.ObtenerEntradaHistoriaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import com.equipo.wikiastrolabio.ui.navigation.Ruta
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EntradaHistoriaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val obtenerEntrada: ObtenerEntradaHistoriaUseCase,
) : ContenidoViewModel<LecturaHistoria>() {

    private val slug = savedStateHandle.toRoute<Ruta.EntradaHistoriaDetalle>().slug

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerEntrada(slug, forzarRed)
}
