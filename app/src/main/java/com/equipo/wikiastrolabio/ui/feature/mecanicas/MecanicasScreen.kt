package com.equipo.wikiastrolabio.ui.feature.mecanicas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Habilidad
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Regla
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.pixel.IconoEstrellaGuia
import com.equipo.wikiastrolabio.ui.feature.mecanicas.components.TarjetaHabilidad
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
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

/** Manual de campo de Almanaque XII: habilidades jugables, acciones narrativas y reglas. */
@Composable
fun MecanicasScreen(
    uiState: UiState<Mecanicas>,
    onAtras: () -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        modifier = modifier,
        encabezado = {
            EncabezadoPantalla(
                titulo = stringResource(R.string.mecanicas_titulo),
                antetitulo = stringResource(R.string.mecanicas_antetitulo),
                subtitulo = stringResource(R.string.mecanicas_subtitulo),
                onAtras = onAtras,
                principal = true,
            )
        },
    ) { mecanicas ->
        if (mecanicas.jugables.isNotEmpty()) {
            item(key = "jugables-titulo") {
                EtiquetaSeccion(stringResource(R.string.mecanicas_habilidades), numero = 1)
            }
            items(mecanicas.jugables, key = { it.slug }) { habilidad -> TarjetaHabilidad(habilidad) }
        }
        if (mecanicas.narrativas.isNotEmpty()) {
            item(key = "narrativas-titulo") {
                EtiquetaSeccion(stringResource(R.string.mecanicas_acciones), numero = 2)
            }
            items(mecanicas.narrativas, key = { it.slug }) { habilidad -> TarjetaAccion(habilidad) }
        }
        if (mecanicas.reglas.isNotEmpty()) {
            item(key = "reglas-titulo") {
                EtiquetaSeccion(stringResource(R.string.mecanicas_reglas), numero = 3)
            }
            itemsIndexed(mecanicas.reglas, key = { _, regla -> regla.slug }) { indice, regla -> TarjetaRegla(indice + 1, regla) }
        }
    }
}

/** Acción narrativa (Observar, Descansar…): más sencilla que una habilidad jugable. */
@Composable
private fun TarjetaAccion(habilidad: Habilidad) {
    PanelPixel {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m), verticalAlignment = Alignment.CenterVertically) {
            IconoEstrellaGuia(tamano = 18.dp, color = LocalAcento.current)
            Text(habilidad.nombre.uppercase(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
        }
        Text(habilidad.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun TarjetaRegla(numero: Int, regla: Regla) {
    PanelPixel {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m)) {
            Text(
                text = numero.toString().padStart(2, '0'),
                style = MaterialTheme.typography.titleLarge,
                color = LocalAcento.current,
                modifier = Modifier.width(32.dp),
            )
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(regla.nombre.uppercase(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                Text(regla.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1400)
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
