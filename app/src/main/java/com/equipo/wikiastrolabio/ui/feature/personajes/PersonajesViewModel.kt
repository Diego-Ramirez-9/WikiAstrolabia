package com.equipo.wikiastrolabio.ui.feature.personajes

import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.usecase.personajes.ObtenerPersonajesUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PersonajesViewModel @Inject constructor(
    private val obtenerPersonajes: ObtenerPersonajesUseCase,
) : ContenidoViewModel<List<PersonajeResumen>>() {

    init {
        cargar()
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerPersonajes(forzarRed)
}
