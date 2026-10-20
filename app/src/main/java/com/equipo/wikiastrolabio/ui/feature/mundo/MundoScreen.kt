package com.equipo.wikiastrolabio.ui.feature.mundo

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.common.colorDeTipo
import com.equipo.wikiastrolabio.ui.components.BotonPixel
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.FilaIndice
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.mundo.components.MapaGranReloj
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.ConAcento
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

/**
 * El Gran Reloj: mapa con un marcador por zona, ficha del lugar elegido y la lista de lugares.
 * Tocar un marcador o una fila lo selecciona; "Explorar" abre la zona.
 */
@Composable
fun MundoScreen(
    uiState: UiState<Mapa>,
    onAtras: () -> Unit,
    onExplorarZona: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Lugar elegido: estado visual, se conserva al girar la pantalla.
    var slugElegido by rememberSaveable { mutableStateOf<String?>(null) }

    PantallaContenido(
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        modifier = modifier,
        encabezado = { mapa ->
            EncabezadoPantalla(
                titulo = mapa?.titulo ?: stringResource(R.string.mundo_titulo),
                antetitulo = stringResource(R.string.mundo_antetitulo),
                subtitulo = mapa?.descripcion,
                onAtras = onAtras,
                principal = true,
            )
        },
    ) { mapa ->
        // Sin elección, se muestra el primer lugar para que la ficha nunca quede vacía.
        val elegido = mapa.puntos.firstOrNull { it.slug == slugElegido } ?: mapa.puntos.firstOrNull()

        item(key = "mapa") {
            MapaGranReloj(
                mapa = mapa,
                slugSeleccionado = elegido?.slug,
                onPuntoClick = { slugElegido = it.slug },
            )
        }
        item(key = "pista") {
            Text(
                text = stringResource(R.string.mundo_toca_marcador).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (elegido != null) {
            item(key = "ficha") {
                FichaLugar(
                    punto = elegido,
                    numero = mapa.puntos.indexOf(elegido) + 1,
                    onExplorar = { onExplorarZona(elegido.slug) },
                )
            }
        }
        item(key = "lugares") {
            EtiquetaSeccion(
                texto = stringResource(R.string.mundo_lugares),
                detalle = mapa.puntos.size.toString().padStart(2, '0'),
            )
        }
        itemsIndexed(mapa.puntos, key = { _, punto -> punto.slug }) { indice, punto ->
            ConAcento(colorDeTipo(punto.tipo)) {
                Column {
                    FilaIndice(
                        titulo = punto.nombre,
                        subtitulo = punto.subtitulo,
                        numero = indice + 1,
                        onClick = { onExplorarZona(punto.slug) },
                        insignias = { Insignia(stringResource(textoTipo(punto.tipo))) },
                    )
                    Divisor()
                }
            }
        }
    }
}

/** Ficha del lugar elegido en el mapa, con el color de su tipo. */
@Composable
private fun FichaLugar(punto: PuntoMapa, numero: Int, onExplorar: () -> Unit) {
    val color = colorDeTipo(punto.tipo)
    ConAcento(color) {
        PanelPixel(resaltado = true, separacion = Espaciado.s) {
            Text(
                text = stringResource(
                    R.string.mundo_localizacion,
                    numero.toString().padStart(2, '0'),
                    stringResource(textoTipo(punto.tipo)),
                ).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = color,
            )
            Text(punto.nombre.uppercase(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
            punto.subtitulo?.let {
                Text(it, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.tertiary)
            }
            punto.resumen?.let {
                Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            BotonPixel(
                texto = stringResource(R.string.mundo_explorar),
                onClick = onExplorar,
                modifier = Modifier.padding(top = Espaciado.s),
            )
        }
    }
}

@StringRes
fun textoTipo(tipo: TipoZona): Int = when (tipo) {
    TipoZona.EJE -> R.string.tipo_zona_eje
    TipoZona.CASA -> R.string.tipo_zona_casa
    TipoZona.CIERRE -> R.string.tipo_zona_cierre
    TipoZona.DESCONOCIDO -> R.string.tipo_zona_desconocido
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1200)
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
