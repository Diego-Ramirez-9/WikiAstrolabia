package com.equipo.wikiastrolabio.ui.feature.salas

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.equipo.wikiastrolabio.domain.model.RecorridoSala
import com.equipo.wikiastrolabio.domain.usecase.mundo.ObtenerSalaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import com.equipo.wikiastrolabio.ui.navigation.Ruta
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SalaViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val obtenerSala: ObtenerSalaUseCase,
) : ContenidoViewModel<RecorridoSala>() {

    private val slug = savedStateHandle.toRoute<Ruta.SalaDetalle>().slug

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerSala(slug, forzarRed)
}
