package com.equipo.wikiastrolabio.ui.feature.mecanicas

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
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.mecanicas.components.TarjetaHabilidad
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun MecanicasRoute(
    onAtras: () -> Unit,
    viewModel: MecanicasViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MecanicasScreen(
        uiState = uiState,
        onAtras = onAtras,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/**
 * El Figma solo muestra las habilidades jugables; las acciones narrativas y las reglas se
 * agregan al final (la API ya las entrega). UI/UX decide si se quedan.
 */
@Composable
fun MecanicasScreen(
    uiState: UiState<Mecanicas>,
    onAtras: () -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = stringResource(R.string.mecanicas_titulo),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { mecanicas ->
        item { Text(stringResource(R.string.mecanicas_subtitulo), style = MaterialTheme.typography.bodyMedium) }
        items(mecanicas.jugables, key = { it.slug }) { habilidad -> TarjetaHabilidad(habilidad) }

        if (mecanicas.narrativas.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.mecanicas_acciones)) }
            items(mecanicas.narrativas, key = { it.slug }) { habilidad -> BloqueTexto(habilidad.nombre, habilidad.descripcion) }
        }
        if (mecanicas.reglas.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.mecanicas_reglas)) }
            items(mecanicas.reglas, key = { it.slug }) { regla -> BloqueTexto(regla.nombre, regla.descripcion) }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MecanicasScreenPreview() {
    WikiAstrolabioTheme {
        MecanicasScreen(
            uiState = UiState.Exito(DatosPreview.mecanicas),
            onAtras = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
