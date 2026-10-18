package com.equipo.wikiastrolabio.ui.feature.personajes

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.usecase.personajes.ObtenerPersonajeUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import com.equipo.wikiastrolabio.ui.navigation.Ruta
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonajeDetalleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val obtenerPersonaje: ObtenerPersonajeUseCase,
) : ContenidoViewModel<Personaje>() {

    private val slug = savedStateHandle.toRoute<Ruta.PersonajeDetalle>().slug

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerPersonaje(slug, forzarRed)
}
