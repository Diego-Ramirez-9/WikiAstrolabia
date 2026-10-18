package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.archivos.components.AcordeonCategoria
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ArchivosRoute(
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    viewModel: ArchivosViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArchivosScreen(
        uiState = uiState,
        onAtras = onAtras,
        onArchivoClick = onArchivoClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun ArchivosScreen(
    uiState: UiState<List<CategoriaArchivo>>,
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Qué acordeones están abiertos. Es estado puramente visual: vive en la UI, no en el
    // ViewModel, y rememberSaveable lo conserva al girar la pantalla. null = aún sin tocar.
    var abiertas by rememberSaveable { mutableStateOf<Set<String>?>(null) }

    PantallaContenido(
        titulo = stringResource(R.string.archivos_titulo),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { categorias ->
        // Como en el Figma, la primera colección empieza abierta.
        val expandidas = abiertas ?: setOfNotNull(categorias.firstOrNull()?.slug)
        item { Text(stringResource(R.string.archivos_subtitulo), style = MaterialTheme.typography.bodyMedium) }
        items(categorias, key = { it.slug }) { categoria ->
            AcordeonCategoria(
                categoria = categoria,
                expandida = categoria.slug in expandidas,
                onAlternar = {
                    abiertas = if (categoria.slug in expandidas) expandidas - categoria.slug else expandidas + categoria.slug
                },
                onArchivoClick = onArchivoClick,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchivosScreenPreview() {
    WikiAstrolabioTheme {
        ArchivosScreen(
            uiState = UiState.Exito(DatosPreview.categoriasArchivo),
            onAtras = {},
            onArchivoClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
