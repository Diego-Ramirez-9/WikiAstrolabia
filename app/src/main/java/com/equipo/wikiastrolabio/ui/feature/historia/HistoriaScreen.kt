package com.equipo.wikiastrolabio.ui.feature.historia

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
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
import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
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

/**
 * Pantalla de Historia rediseñada con la misma estética retro de Figma:
 * - Botón atrás retro BotonAtrasPixel (cuadrado con borde ámbar).
 * - Título "HISTORIA" en tipografía pixel/serif retro.
 * - Subtítulo descriptivo en tono Sand-300.
 * - Secciones de cosmología con separador técnico.
 * - Tarjetas técnicas en Obsidian-900 con borde Slate-800 y flecha ámbar.
 */
@Composable
fun HistoriaScreen(
    uiState: UiState<List<SeccionHistoria>>,
    onAtras: () -> Unit,
    onEntradaClick: (slug: String) -> Unit,
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
        ) { secciones ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // 1. Botón atrás retro cuadrado unificado
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "HISTORIA" y subtítulo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "HISTORIA",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FuentePixelTitulosFallback,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 26.sp,
                            ),
                            color = Blanco,
                        )

                        Text(
                            text = stringResource(R.string.codex_historia_desc),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FuentePixelFallback,
                                fontSize = 12.5.sp,
                                letterSpacing = 0.4.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }

                // 3. Secciones y entradas
                secciones.forEach { seccion ->
                    item(key = "seccion-${seccion.slug}") {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp, bottom = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = seccion.titulo.uppercase(),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FuentePixelFallback,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.5.sp,
                                ),
                                color = Sand300,
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                thickness = 1.dp,
                                color = Slate800,
                            )
                        }
                    }

                    items(seccion.entradas, key = { it.slug }) { entrada ->
                        TarjetaEntradaHistoria(
                            entrada = entrada,
                            onClick = { onEntradaClick(entrada.slug) },
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/**
 * Tarjeta de entrada histórica con la estética unificada de Figma
 */
@Composable
private fun TarjetaEntradaHistoria(
    entrada: EntradaHistoria,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Obsidian900, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = entrada.titulo,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FuentePixelTitulosFallback,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 17.sp,
                    letterSpacing = 1.sp,
                ),
                color = TerraCotta500,
            )

            Text(
                text = entrada.contenido,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = FuentePixelFallback,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    letterSpacing = 0.3.sp,
                ),
                color = Cream200,
            )

            HorizontalDivider(
                thickness = 1.dp,
                color = Slate800,
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "LEER ENTRADA",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FuentePixelFallback,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp,
                    ),
                    color = Ambar400,
                )

                Icon(
                    painter = painterResource(R.drawable.ic_flecha_derecha_pixel),
                    contentDescription = null,
                    tint = Ambar400,
                    modifier = Modifier.size(14.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true)
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
