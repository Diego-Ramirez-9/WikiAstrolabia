package com.equipo.wikiastrolabio.ui.feature.personajes

import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.personajes.components.TarjetaPersonaje
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun PersonajesRoute(
    onAtras: () -> Unit,
    onPersonajeClick: (slug: String) -> Unit,
    viewModel: PersonajesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PersonajesScreen(
        uiState = uiState,
        onAtras = onAtras,
        onPersonajeClick = onPersonajeClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun PersonajesScreen(
    uiState: UiState<List<PersonajeResumen>>,
    onAtras: () -> Unit,
    onPersonajeClick: (slug: String) -> Unit,
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
                titulo = stringResource(R.string.personajes_titulo),
                antetitulo = stringResource(R.string.personajes_antetitulo),
                subtitulo = stringResource(R.string.personajes_subtitulo),
                onAtras = onAtras,
                principal = true,
            )
        },
    ) { personajes ->
        itemsIndexed(personajes, key = { _, personaje -> personaje.slug }) { indice, personaje ->
            TarjetaPersonaje(
                personaje = personaje,
                numero = indice + 1,
                onClick = { onPersonajeClick(personaje.slug) },
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 900)
@Composable
private fun PersonajesScreenPreview() {
    WikiAstrolabioTheme {
        PersonajesScreen(
            uiState = UiState.Exito(DatosPreview.personajes),
            onAtras = {},
            onPersonajeClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
