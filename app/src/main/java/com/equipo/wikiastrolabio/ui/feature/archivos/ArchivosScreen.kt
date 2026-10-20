package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.compose.foundation.lazy.itemsIndexed
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
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
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

/** Biblioteca de coleccionables: cada categoría de la API es una colección desplegable. */
@Composable
fun ArchivosScreen(
    uiState: UiState<List<CategoriaArchivo>>,
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Colecciones abiertas: estado visual que sobrevive al giro. null = aún sin tocar.
    var abiertas by rememberSaveable { mutableStateOf<Set<String>?>(null) }

    PantallaContenido(
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        modifier = modifier,
        encabezado = {
            EncabezadoPantalla(
                titulo = stringResource(R.string.archivos_titulo),
                antetitulo = stringResource(R.string.archivos_antetitulo),
                subtitulo = stringResource(R.string.archivos_subtitulo),
                onAtras = onAtras,
                principal = true,
            )
        },
    ) { categorias ->
        // La primera colección empieza abierta para que la pantalla nunca se vea vacía.
        val expandidas = abiertas ?: setOfNotNull(categorias.firstOrNull()?.slug)
        item(key = "colecciones") {
            EtiquetaSeccion(
                texto = stringResource(R.string.archivos_colecciones),
                detalle = categorias.size.toString().padStart(2, '0'),
            )
        }
        itemsIndexed(categorias, key = { _, categoria -> categoria.slug }) { indice, categoria ->
            AcordeonCategoria(
                categoria = categoria,
                indice = indice + 1,
                expandida = categoria.slug in expandidas,
                onAlternar = {
                    abiertas = if (categoria.slug in expandidas) expandidas - categoria.slug else expandidas + categoria.slug
                },
                onArchivoClick = onArchivoClick,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 900)
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
