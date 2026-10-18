package com.equipo.wikiastrolabio.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.wikiastrolabio.domain.model.Resultado
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Base de los ViewModel que cargan un solo bloque de contenido (casi todas las pantallas).
 *
 * Uso:
 * ```
 * @HiltViewModel
 * class PersonajesViewModel @Inject constructor(
 *     private val obtenerPersonajes: ObtenerPersonajesUseCase,
 * ) : ContenidoViewModel<List<PersonajeResumen>>() {
 *     init { cargar() }   // siempre en el init de la subclase, nunca en esta clase base
 *     override suspend fun obtener(forzarRed: Boolean) = obtenerPersonajes(forzarRed)
 * }
 * ```
 */
abstract class ContenidoViewModel<T> : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<T>>(UiState.Cargando)
    val uiState: StateFlow<UiState<T>> = _uiState.asStateFlow()

    private var carga: Job? = null

    protected abstract suspend fun obtener(forzarRed: Boolean): Resultado<T>

    /** Botón "Reintentar" de la pantalla de error. */
    fun reintentar() = cargar()

    /** Deslizar para recargar: ignora la caché y pide a la API. */
    fun recargar() = cargar(forzarRed = true)

    protected fun cargar(forzarRed: Boolean = false) {
        carga?.cancel()
        carga = viewModelScope.launch {
            val actual = _uiState.value
            _uiState.value = if (actual is UiState.Exito) actual.copy(recargando = true) else UiState.Cargando
            _uiState.value = obtener(forzarRed).aUiState()
        }
    }
}
