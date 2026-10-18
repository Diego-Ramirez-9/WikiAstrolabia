package com.equipo.wikiastrolabio.ui.feature.historia

import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.usecase.historia.ObtenerHistoriaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoriaViewModel @Inject constructor(
    private val obtenerHistoria: ObtenerHistoriaUseCase,
) : ContenidoViewModel<List<SeccionHistoria>>() {

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerHistoria(forzarRed)
}
