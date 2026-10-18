package com.equipo.wikiastrolabio.ui.common

import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.OrigenDatos
import com.equipo.wikiastrolabio.domain.model.Resultado

/** Estado de cualquier pantalla que muestra contenido de la API. */
sealed interface UiState<out T> {

    data object Cargando : UiState<Nothing>

    data class Exito<out T>(
        val datos: T,
        /** Se muestra contenido guardado porque la API no respondió. */
        val sinConexion: Boolean = false,
        /** Hay una recarga en curso; los datos anteriores siguen visibles. */
        val recargando: Boolean = false,
    ) : UiState<T>

    data class Error(val error: ErrorDatos) : UiState<Nothing>
}

fun <T> Resultado<T>.aUiState(): UiState<T> = when (this) {
    is Resultado.Exito -> UiState.Exito(datos, sinConexion = origen == OrigenDatos.CACHE_SIN_CONEXION)
    is Resultado.Error -> UiState.Error(error)
}
