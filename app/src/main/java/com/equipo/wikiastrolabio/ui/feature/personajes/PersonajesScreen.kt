package com.equipo.wikiastrolabio.ui.feature.personajes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.feature.personajes.components.TarjetaPersonaje
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Sand300
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

/**
 * Pantalla de Personajes rediseñada según el prototipo de Figma:
 * - Botón atrás retro en cuadrado con borde ámbar.
 * - Título "PERSONAJES" en tipografía pixel/serif retro.
 * - Subtítulo "Quienes habitan entre los engranajes.".
 * - Catálogo de tarjetas de personajes con etiquetas PERSONAJE 01, 02...
 * - Contenedor técnico de ilustración y acción "VER MÁS ➔".
 */
@Composable
fun PersonajesScreen(
    uiState: UiState<List<PersonajeResumen>>,
    onAtras: () -> Unit,
    onPersonajeClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
    ) {
        ContenidoSegunEstado(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
        ) { personajes ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                // 1. Botón atrás retro cuadrado con borde ámbar
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "PERSONAJES" y subtítulo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "PERSONAJES",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FuentePixelTitulosFallback,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 26.sp,
                            ),
                            color = Blanco,
                        )

                        Text(
                            text = stringResource(R.string.personajes_subtitulo),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FuentePixelFallback,
                                fontSize = 12.5.sp,
                                letterSpacing = 0.4.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }

                // 3. Catálogo de tarjetas de personajes
                itemsIndexed(personajes, key = { _, personaje -> personaje.slug }) { index, personaje ->
                    TarjetaPersonaje(
                        personaje = personaje,
                        indice = index + 1,
                        onClick = { onPersonajeClick(personaje.slug) },
                    )
                }

                // Espacio extra al final para scroll holgado sobre la barra inferior
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
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
