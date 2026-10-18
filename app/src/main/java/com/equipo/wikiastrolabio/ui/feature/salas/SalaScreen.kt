package com.equipo.wikiastrolabio.ui.feature.salas

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueDialogo
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.TarjetaNavegable
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun SalaRoute(
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    viewModel: SalaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SalaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onArchivoClick = onArchivoClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun SalaScreen(
    uiState: UiState<Sala>,
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = (uiState as? UiState.Exito)?.datos?.nombre.orEmpty(),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { sala ->
        sala.zona?.let { zona -> item { Text(zona.nombre, style = MaterialTheme.typography.labelMedium) } }
        if (sala.imagenUrl != null) {
            item {
                ImagenPixelArt(
                    url = sala.imagenUrl,
                    contentDescription = sala.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f),
                )
            }
        }
        item { BloqueTexto(stringResource(R.string.sala_descripcion), sala.descripcion) }
        item { BloqueTexto(stringResource(R.string.sala_aprendizaje), sala.aprendizaje) }

        sala.observatorio?.let { observatorio ->
            item { EncabezadoSeccion(observatorio.nombre) }
            item { BloqueTexto(stringResource(R.string.sala_exterior), observatorio.exterior) }
            item { BloqueTexto(stringResource(R.string.sala_interior), observatorio.interior) }
            item { BloqueTexto(stringResource(R.string.sala_luz), observatorio.luz) }
            item { BloqueTexto(stringResource(R.string.sala_descubrimientos), observatorio.descubrimientos) }
        }

        if (sala.archivos.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.sala_archivos)) }
            items(sala.archivos, key = { it.slug }) { archivo ->
                TarjetaNavegable(titulo = archivo.titulo, onClick = { onArchivoClick(archivo.slug) })
            }
        }

        if (sala.dialogos.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.personaje_dialogos)) }
            items(sala.dialogos, key = { it.slug }) { dialogo -> BloqueDialogo(dialogo) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SalaScreenPreview() {
    WikiAstrolabioTheme {
        SalaScreen(
            uiState = UiState.Exito(DatosPreview.sala),
            onAtras = {},
            onArchivoClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
