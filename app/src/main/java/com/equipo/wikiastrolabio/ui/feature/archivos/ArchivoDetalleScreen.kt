package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ArchivoDetalleRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: ArchivoDetalleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArchivoDetalleScreen(
        uiState = uiState,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun ArchivoDetalleScreen(
    uiState: UiState<Archivo>,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
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
    ) { archivo ->
        if (archivo.imagenUrl != null) {
            item {
                ImagenPixelArt(
                    url = archivo.imagenUrl,
                    contentDescription = archivo.titulo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(4f / 3f),
                )
            }
        }
        item { Text(archivo.contenido, style = MaterialTheme.typography.bodyLarge, fontStyle = FontStyle.Italic) }
        archivo.autor?.let { autor ->
            item { Text(stringResource(R.string.archivo_autor, autor), style = MaterialTheme.typography.labelLarge) }
        }
        item { BloqueTexto(stringResource(R.string.archivo_ubicacion), archivo.ubicacion) }
        archivo.sala?.let { sala ->
            item {
                OutlinedButton(onClick = { onSalaClick(sala.slug) }) {
                    Text(stringResource(R.string.archivo_ver_sala, sala.nombre))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchivoDetalleScreenPreview() {
    WikiAstrolabioTheme {
        ArchivoDetalleScreen(
            uiState = UiState.Exito(DatosPreview.archivo),
            onAtras = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
