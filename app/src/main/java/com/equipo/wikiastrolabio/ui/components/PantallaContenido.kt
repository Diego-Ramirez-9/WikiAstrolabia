package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.theme.Espaciado

/**
 * Esqueleto común de las pantallas: barra superior + estados (carga/error) + lista desplazable.
 * El contenido se describe con el DSL de LazyColumn (`item { }`, `items(lista) { }`), así las
 * pantallas largas solo componen lo que está a la vista.
 */
@Composable
fun <T> PantallaContenido(
    titulo: String,
    uiState: UiState<T>,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
    onAtras: (() -> Unit)? = null,
    onRecargar: (() -> Unit)? = null,
    contenido: LazyListScope.(T) -> Unit,
) {
    Column(modifier = modifier.fillMaxSize()) {
        BarraSuperior(titulo = titulo, onAtras = onAtras)
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
                    bottom = Espaciado.l,
                ),
                verticalArrangement = Arrangement.spacedBy(Espaciado.s),
            ) {
                contenido(datos)
            }
        }
    }
}
