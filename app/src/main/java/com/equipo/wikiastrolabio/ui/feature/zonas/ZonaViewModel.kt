package com.equipo.wikiastrolabio.ui.feature.zonas

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.domain.usecase.mundo.ObtenerCasasUseCase
import com.equipo.wikiastrolabio.domain.usecase.mundo.ObtenerZonaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import com.equipo.wikiastrolabio.ui.navigation.Ruta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Detalle de una zona + pestañas para cambiar entre Casas sin regresar al mapa.
 * La zona elegida se guarda en [SavedStateHandle] para sobrevivir a la muerte del proceso.
 */
@HiltViewModel
class ZonaViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val obtenerZona: ObtenerZonaUseCase,
    private val obtenerCasas: ObtenerCasasUseCase,
) : ContenidoViewModel<Zona>() {

    val slugSeleccionado: StateFlow<String> = savedStateHandle.getStateFlow(
        CLAVE_SELECCION,
        savedStateHandle.toRoute<Ruta.ZonaDetalle>().slug,
    )

    private val _pestanas = MutableStateFlow<List<ZonaResumen>>(emptyList())

    /** Casas para las pestañas; vacío si no cargaron (la pantalla funciona sin ellas). */
    val pestanas: StateFlow<List<ZonaResumen>> = _pestanas.asStateFlow()

    init {
        cargar()
        viewModelScope.launch {
            (obtenerCasas() as? Resultado.Exito)?.let { _pestanas.value = it.datos }
        }
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerZona(slugSeleccionado.value, forzarRed)

    fun seleccionarZona(slug: String) {
        if (slug == slugSeleccionado.value) return
        savedStateHandle[CLAVE_SELECCION] = slug
        cargar()
    }

    private companion object {
        const val CLAVE_SELECCION = "zona_seleccionada"
    }
}
