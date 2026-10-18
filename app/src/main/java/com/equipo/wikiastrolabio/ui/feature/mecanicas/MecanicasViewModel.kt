package com.equipo.wikiastrolabio.ui.feature.mecanicas

import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.usecase.mecanicas.ObtenerMecanicasUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MecanicasViewModel @Inject constructor(
    private val obtenerMecanicas: ObtenerMecanicasUseCase,
) : ContenidoViewModel<Mecanicas>() {

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerMecanicas(forzarRed)
}
