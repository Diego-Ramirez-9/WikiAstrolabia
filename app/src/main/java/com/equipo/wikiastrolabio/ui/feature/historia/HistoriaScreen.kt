package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.TarjetaNavegable
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Punto de entrada desde la navegación: obtiene el ViewModel y su estado. */
@Composable
fun HistoriaRoute(
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
    viewModel: HistoriaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoriaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onEntradaClick = onEntradaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/** Pantalla sin lógica: solo dibuja el estado. Es la que se diseña y la que usan las vistas previas. */
@Composable
fun HistoriaScreen(
    uiState: UiState<List<SeccionHistoria>>,
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = stringResource(R.string.historia_titulo),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { secciones ->
        secciones.forEach { seccion ->
            item(key = "seccion-${seccion.slug}") { EncabezadoSeccion(seccion.titulo) }
            items(seccion.entradas, key = { it.slug }) { entrada ->
                TarjetaNavegable(
                    titulo = entrada.titulo,
                    subtitulo = entrada.contenido,
                    onClick = { onEntradaClick(entrada.slug) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HistoriaScreenPreview() {
    WikiAstrolabioTheme {
        HistoriaScreen(
            uiState = UiState.Exito(DatosPreview.historia),
            onAtras = {},
            onEntradaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
