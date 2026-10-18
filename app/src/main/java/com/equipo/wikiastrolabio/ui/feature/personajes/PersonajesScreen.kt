package com.equipo.wikiastrolabio.ui.feature.personajes

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
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.ui.common.UiState
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
        titulo = stringResource(R.string.personajes_titulo),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { personajes ->
        item { Text(stringResource(R.string.personajes_subtitulo), style = MaterialTheme.typography.bodyMedium) }
        items(personajes, key = { it.slug }) { personaje ->
            TarjetaPersonaje(personaje = personaje, onClick = { onPersonajeClick(personaje.slug) })
        }
    }
}

@Preview(showBackground = true)
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
