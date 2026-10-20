package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.theme.Espaciado

/**
 * Plantilla de todas las pantallas con contenido de la API.
 *
 * - Mientras carga o si hay error, el encabezado queda fijo arriba (con su botón de regresar)
 *   y el estado ocupa el resto.
 * - Con datos, el encabezado es el primer elemento de la lista y se desplaza con el contenido.
 *
 * [encabezado] recibe los datos cuando ya existen (null mientras carga), para que el título
 * de un detalle pueda salir de la API.
 */
@Composable
fun <T> PantallaContenido(
    uiState: UiState<T>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onRecargar: (() -> Unit)? = null,
    encabezado: @Composable (datos: T?) -> Unit,
    contenido: LazyListScope.(datos: T) -> Unit,
) {
    val margen = Modifier.padding(horizontal = Espaciado.margenPantalla)
    Column(modifier = modifier.fillMaxSize()) {
        if (uiState !is UiState.Exito) {
            Column(margen) { encabezado(null) }
        }
        ContenidoSegunEstado(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
        ) { datos ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = Espaciado.margenPantalla,
                    end = Espaciado.margenPantalla,
                    bottom = Espaciado.xxxl,
                ),
                verticalArrangement = Arrangement.spacedBy(Espaciado.m),
            ) {
                item(key = "encabezado") { encabezado(datos) }
                contenido(datos)
            }
        }
    }
}
