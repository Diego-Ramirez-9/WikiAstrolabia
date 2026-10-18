package com.equipo.wikiastrolabio.ui.feature.zonas

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.Etiquetas
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.zonas.components.FilaSala
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ZonaRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: ZonaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pestanas by viewModel.pestanas.collectAsStateWithLifecycle()
    val slugSeleccionado by viewModel.slugSeleccionado.collectAsStateWithLifecycle()
    ZonaScreen(
        uiState = uiState,
        pestanas = pestanas,
        slugSeleccionado = slugSeleccionado,
        onPestanaClick = viewModel::seleccionarZona,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun ZonaScreen(
    uiState: UiState<Zona>,
    pestanas: List<ZonaResumen>,
    slugSeleccionado: String,
    onPestanaClick: (slug: String) -> Unit,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = stringResource(R.string.zona_titulo),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { zona ->
        // Las pestañas solo tienen sentido si la zona abierta es una de las Casas.
        val indice = pestanas.indexOfFirst { it.slug == slugSeleccionado }
        if (indice >= 0) {
            item {
                PrimaryScrollableTabRow(selectedTabIndex = indice) {
                    pestanas.forEach { casa ->
                        Tab(
                            selected = casa.slug == slugSeleccionado,
                            onClick = { onPestanaClick(casa.slug) },
                            text = { Text(casa.nombre) },
                        )
                    }
                }
            }
        }
        item {
            ImagenPixelArt(
                url = zona.imagenUrl,
                contentDescription = zona.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
        }
        item { Text(zona.nombre, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.primary) }
        zona.subtitulo?.let { item { Text(it, style = MaterialTheme.typography.titleMedium) } }
        zona.constelacion?.let { constelacion ->
            item {
                val nombre = listOfNotNull(constelacion.nombre, constelacion.sobrenombre).joinToString(" · ")
                Text(stringResource(R.string.zona_constelacion, nombre), style = MaterialTheme.typography.labelMedium)
            }
        }
        item { Etiquetas(zona.etiquetas) }
        item { BloqueTexto(stringResource(R.string.zona_resumen), zona.resumen) }

        if (zona.salas.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.zona_habitaciones, zona.salas.size)) }
            items(zona.salas, key = { it.slug }) { sala ->
                FilaSala(sala = sala, onClick = { onSalaClick(sala.slug) })
            }
        }

        item { BloqueTexto(stringResource(R.string.zona_exterior), zona.exterior) }
        item { BloqueTexto(stringResource(R.string.zona_ambientacion), zona.ambientacion) }
        item { BloqueTexto(stringResource(R.string.zona_narrativa), zona.narrativa) }
        item { BloqueTexto(stringResource(R.string.zona_restauracion), zona.restauracion) }

        if (zona.mecanicas.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.zona_mecanicas)) }
            items(zona.mecanicas, key = { it.slug }) { mecanica -> BloqueTexto(mecanica.nombre, mecanica.descripcion) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ZonaScreenPreview() {
    WikiAstrolabioTheme {
        ZonaScreen(
            uiState = UiState.Exito(DatosPreview.zona),
            pestanas = DatosPreview.casas,
            slugSeleccionado = DatosPreview.zona.slug,
            onPestanaClick = {},
            onAtras = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
