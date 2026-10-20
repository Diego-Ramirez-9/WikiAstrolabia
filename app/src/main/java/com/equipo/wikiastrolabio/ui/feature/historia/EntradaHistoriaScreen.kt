package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.LecturaHistoria
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.NavegacionVecinos
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.TextoLectura
import com.equipo.wikiastrolabio.ui.components.Vecino
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun EntradaHistoriaRoute(
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
    viewModel: EntradaHistoriaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    EntradaHistoriaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onEntradaClick = onEntradaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/** Vista de lectura: capítulo, ilustración, texto con capitular y paso a la entrada vecina. */
@Composable
fun EntradaHistoriaScreen(
    uiState: UiState<LecturaHistoria>,
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
        encabezado = { lectura ->
            val capitulo = lectura?.entrada?.seccion?.titulo
            val numero = lectura?.numeroCapitulo
            EncabezadoPantalla(
                titulo = lectura?.entrada?.titulo.orEmpty(),
                antetitulo = when {
                    capitulo != null && numero != null ->
                        stringResource(R.string.historia_capitulo_y_titulo, numero.toString().padStart(2, '0'), capitulo)
                    capitulo != null -> capitulo
                    else -> stringResource(R.string.historia_titulo)
                },
                onAtras = onAtras,
            )
        },
    ) { lectura ->
        val entrada = lectura.entrada
        item(key = "ilustracion") {
            MarcoIlustracion(url = entrada.imagenUrl, descripcion = entrada.titulo, semilla = entrada.slug)
        }
        item(key = "texto") {
            TextoLectura(entrada.contenido, capitular = true, modifier = Modifier.padding(top = Espaciado.s))
        }
        item(key = "vecinos") {
            NavegacionVecinos(
                anterior = lectura.anterior?.let {
                    Vecino(stringResource(R.string.navegacion_anterior), it.titulo) { onEntradaClick(it.slug) }
                },
                siguiente = lectura.siguiente?.let {
                    Vecino(stringResource(R.string.navegacion_siguiente), it.titulo) { onEntradaClick(it.slug) }
                },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 900)
@Composable
private fun EntradaHistoriaScreenPreview() {
    WikiAstrolabioTheme {
        EntradaHistoriaScreen(
            uiState = UiState.Exito(DatosPreview.lectura),
            onAtras = {},
            onEntradaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
