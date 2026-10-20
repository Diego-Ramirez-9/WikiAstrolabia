package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.ChipEnlace
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.FilaInsignias
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.TextoLectura
import com.equipo.wikiastrolabio.ui.components.pixel.esquinasTecnicas
import com.equipo.wikiastrolabio.ui.feature.archivos.components.textoCategoria
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ArchivoDetalleRoute(
    onAtras: () -> Unit,
    onZonaClick: (slug: String) -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: ArchivoDetalleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArchivoDetalleScreen(
        uiState = uiState,
        onAtras = onAtras,
        onZonaClick = onZonaClick,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/** Un poema o nota: el documento en un pergamino, su autor y dónde encontrarlo en el juego. */
@Composable
fun ArchivoDetalleScreen(
    uiState: UiState<Archivo>,
    onAtras: () -> Unit,
    onZonaClick: (slug: String) -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        modifier = modifier,
        encabezado = { archivo ->
            EncabezadoPantalla(
                titulo = archivo?.titulo.orEmpty(),
                antetitulo = archivo?.let { textoCategoria(it.categoria) },
                onAtras = onAtras,
            )
        },
    ) { archivo ->
        item(key = "ilustracion") {
            MarcoIlustracion(
                url = archivo.imagenUrl,
                descripcion = archivo.titulo,
                semilla = archivo.slug,
                relacionAspecto = 2f,
            )
        }
        item(key = "documento") { Pergamino(archivo) }

        if (!archivo.ubicacion.isNullOrBlank() || archivo.zona != null || archivo.sala != null) {
            item(key = "ubicacion-titulo") { EtiquetaSeccion(stringResource(R.string.archivo_ubicacion)) }
            item(key = "ubicacion") {
                PanelPixel {
                    archivo.ubicacion?.let {
                        Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
                    }
                    FilaInsignias {
                        archivo.zona?.let { zona -> ChipEnlace(zona.nombre, onClick = { onZonaClick(zona.slug) }) }
                        archivo.sala?.let { sala -> ChipEnlace(sala.nombre, onClick = { onSalaClick(sala.slug) }) }
                    }
                }
            }
        }
    }
}

/** Hoja con borde de escuadras donde se transcribe el documento, firmado por su autor. */
@Composable
private fun Pergamino(archivo: Archivo) {
    val acento = LocalAcento.current
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = Espaciado.s),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier
                .esquinasTecnicas(acento.copy(alpha = 0.6f), largo = 12.dp)
                .padding(horizontal = Espaciado.xl, vertical = Espaciado.xxl),
            verticalArrangement = Arrangement.spacedBy(Espaciado.l),
        ) {
            TextoLectura(archivo.contenido, cursiva = true)
            archivo.autor?.let {
                Text(
                    text = stringResource(R.string.archivo_autor, it),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1000)
@Composable
private fun ArchivoDetalleScreenPreview() {
    WikiAstrolabioTheme {
        ArchivoDetalleScreen(
            uiState = UiState.Exito(DatosPreview.archivo),
            onAtras = {},
            onZonaClick = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
