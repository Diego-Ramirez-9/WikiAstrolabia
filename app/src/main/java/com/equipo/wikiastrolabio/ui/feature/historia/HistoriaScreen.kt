package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.FilaIndice
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun HistoriaRoute(
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
    viewModel: HistoriaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HistoriaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onEntradaClick = onEntradaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/** Índice de la Historia: cada sección de la API es un capítulo con sus entradas numeradas. */
@Composable
fun HistoriaScreen(
    uiState: UiState<List<SeccionHistoria>>,
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
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
                titulo = stringResource(R.string.historia_titulo),
                antetitulo = stringResource(R.string.historia_antetitulo),
                subtitulo = stringResource(R.string.historia_subtitulo),
                onAtras = onAtras,
                principal = true,
            )
        },
    ) { secciones ->
        secciones.forEachIndexed { indice, seccion ->
            item(key = "capitulo-${seccion.slug}") {
                EncabezadoCapitulo(numero = indice + 1, seccion = seccion)
            }
            itemsIndexed(seccion.entradas, key = { _, entrada -> entrada.slug }) { posicion, entrada ->
                Column {
                    FilaIndice(
                        titulo = entrada.titulo,
                        subtitulo = entrada.contenido,
                        numero = posicion + 1,
                        onClick = { onEntradaClick(entrada.slug) },
                    )
                    Divisor()
                }
            }
        }
    }
}

@Composable
private fun EncabezadoCapitulo(numero: Int, seccion: SeccionHistoria) {
    Column(
        modifier = Modifier
            .padding(top = Espaciado.xl)
            .semantics(mergeDescendants = true) { heading() },
        verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
    ) {
        Text(
            text = stringResource(R.string.historia_capitulo, numero.toString().padStart(2, '0')).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = LocalAcento.current,
        )
        Text(
            text = seccion.titulo,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = pluralStringResource(R.plurals.historia_entradas, seccion.entradas.size, seccion.entradas.size).uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Divisor(Modifier.padding(top = Espaciado.s))
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun HistoriaScreenPreview() {
    WikiAstrolabioTheme {
        HistoriaScreen(
            uiState = UiState.Exito(DatosPreview.historia),
            onAtras = {},
            onEntradaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
