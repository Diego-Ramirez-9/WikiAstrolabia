package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun EntradaHistoriaRoute(
    onAtras: () -> Unit,
    viewModel: EntradaHistoriaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EntradaHistoriaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun EntradaHistoriaScreen(
    uiState: UiState<EntradaHistoria>,
    onAtras: () -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = (uiState as? UiState.Exito)?.datos?.titulo.orEmpty(),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { entrada ->
        entrada.seccion?.let { seccion ->
            item { Text(seccion.titulo, style = MaterialTheme.typography.labelMedium) }
        }
        if (entrada.imagenUrl != null) {
            item {
                ImagenPixelArt(
                    url = entrada.imagenUrl,
                    contentDescription = entrada.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
            }
        }
        item { Text(entrada.contenido, style = MaterialTheme.typography.bodyLarge) }
    }
}

@Preview(showBackground = true)
@Composable
private fun EntradaHistoriaScreenPreview() {
    WikiAstrolabioTheme {
        EntradaHistoriaScreen(
            uiState = UiState.Exito(DatosPreview.historia.first().entradas.first()),
            onAtras = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
