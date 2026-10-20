package com.equipo.wikiastrolabio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.equipo.wikiastrolabio.domain.usecase.contenido.SincronizarContenidoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Arranque de la app: consulta en segundo plano si el contenido de la API cambió
 * (GET /contenido/version) para que las pantallas sepan si su copia guardada sigue vigente.
 *
 * No retiene el splash: Inicio no necesita datos de la API, así que la app se muestra en
 * cuanto está lista y la consulta termina mientras el usuario elige una sección.
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    sincronizarContenido: SincronizarContenidoUseCase,
) : ViewModel() {

    init {
        viewModelScope.launch { sincronizarContenido() }
    }
}
