package com.equipo.wikiastrolabio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.wikiastrolabio.domain.usecase.contenido.SincronizarContenidoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

/**
 * Arranque de la app: mientras se ve el splash consulta si el contenido de la API cambió.
 * El splash se quita al terminar o a los [ESPERA_MAXIMA_MS], lo que ocurra primero; si la
 * API tarda (Vercel en frío), la consulta sigue en segundo plano sin bloquear al usuario.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    sincronizarContenido: SincronizarContenidoUseCase,
) : ViewModel() {

    private val _listo = MutableStateFlow(false)
    val listo: StateFlow<Boolean> = _listo.asStateFlow()

    init {
        val sincronizacion = viewModelScope.async { sincronizarContenido() }
        viewModelScope.launch {
            withTimeoutOrNull(ESPERA_MAXIMA_MS) { sincronizacion.await() }
            _listo.value = true
        }
    }

    private companion object {
        const val ESPERA_MAXIMA_MS = 2_500L
    }
}
