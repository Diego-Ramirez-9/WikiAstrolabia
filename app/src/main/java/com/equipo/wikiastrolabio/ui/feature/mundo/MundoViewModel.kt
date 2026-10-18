package com.equipo.wikiastrolabio.ui.feature.mundo

import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.usecase.mundo.ObtenerMapaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MundoViewModel @Inject constructor(
    private val obtenerMapa: ObtenerMapaUseCase,
) : ContenidoViewModel<Mapa>() {

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerMapa(forzarRed)
}
