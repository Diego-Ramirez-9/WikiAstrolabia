package com.equipo.wikiastrolabio.ui.feature.mundo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.mundo.components.MapaGranReloj
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun MundoRoute(
    onAtras: () -> Unit,
    onExplorarZona: (slug: String) -> Unit,
    viewModel: MundoViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MundoScreen(
        uiState = uiState,
        onAtras = onAtras,
        onExplorarZona = onExplorarZona,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun MundoScreen(
    uiState: UiState<Mapa>,
    onAtras: () -> Unit,
    onExplorarZona: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Marcador tocado: estado visual, se conserva al girar la pantalla.
    var slugSeleccionado by rememberSaveable { mutableStateOf<String?>(null) }

    PantallaContenido(
        titulo = (uiState as? UiState.Exito)?.datos?.titulo.orEmpty(),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { mapa ->
        mapa.descripcion?.let { descripcion ->
            item { Text(descripcion, style = MaterialTheme.typography.bodyMedium) }
        }
        item { EncabezadoSeccion(stringResource(R.string.mundo_mapa)) }
        item {
            MapaGranReloj(
                mapa = mapa,
                slugSeleccionado = slugSeleccionado,
                onPuntoClick = { punto -> slugSeleccionado = punto.slug },
                modifier = Modifier.fillMaxWidth(),
            )
        }
        val seleccionado = mapa.puntos.firstOrNull { it.slug == slugSeleccionado }
        item {
            if (seleccionado == null) {
                Text(
                    text = stringResource(R.string.mundo_toca_marcador),
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            } else {
                VistaPreviaZona(punto = seleccionado, onExplorar = { onExplorarZona(seleccionado.slug) })
            }
        }
    }
}

/** Tarjeta con la información del marcador tocado y el acceso a la zona. */
@Composable
private fun VistaPreviaZona(punto: PuntoMapa, onExplorar: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(Espaciado.m),
            verticalArrangement = Arrangement.spacedBy(Espaciado.s),
        ) {
            punto.subtitulo?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
            Text(punto.nombre, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            punto.resumen?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
            Button(onClick = onExplorar) { Text(stringResource(R.string.mundo_explorar)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MundoScreenPreview() {
    WikiAstrolabioTheme {
        MundoScreen(
            uiState = UiState.Exito(DatosPreview.mapa),
            onAtras = {},
            onExplorarZona = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
