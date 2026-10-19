package com.equipo.wikiastrolabio.ui.feature.mecanicas

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
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.feature.mecanicas.components.TarjetaHabilidad
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate800
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
 * Pantalla de Mecánicas rediseñada según el prototipo de Figma:
 * - Botón atrás retro cuadrado con borde ámbar.
 * - Título principal "MECANICAS" en tipografía pixel/serif retro.
 * - Subtítulo "Manual de campo de Almanaque XII.".
 * - Tarjetas técnicas de habilidades jugables (SALTO 01, ANCLA 02, GANCHO 03, ESCALAR 04).
 * - Secciones de acciones narrativas y reglas generales accesibles al desplazar.
 */
@Composable
fun MecanicasScreen(
    uiState: UiState<Mecanicas>,
    onAtras: () -> Unit,
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
        ) { mecanicas ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // 1. Botón atrás retro cuadrado con borde ámbar
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "MECANICAS" y subtítulo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "MECANICAS",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FuentePixelTitulosFallback,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 26.sp,
                            ),
                            color = Blanco,
                        )

                        Text(
                            text = stringResource(R.string.mecanicas_subtitulo),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FuentePixelFallback,
                                fontSize = 12.5.sp,
                                letterSpacing = 0.4.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }

                // 3. Tarjetas de habilidades jugables (Salto, Ancla, Gancho, Escalar...)
                items(mecanicas.jugables, key = { it.slug }) { habilidad ->
                    TarjetaHabilidad(habilidad = habilidad)
                }

                // 4. Acciones narrativas adicionales
                if (mecanicas.narrativas.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "ACCIONES NARRATIVAS",
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

                    items(mecanicas.narrativas, key = { it.slug }) { accion ->
                        BloqueMecanicaRetro(accion.nombre, accion.descripcion)
                    }
                }

                // 5. Reglas generales del mecanismo
                if (mecanicas.reglas.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "REGLAS DEL RELOJ",
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

                    items(mecanicas.reglas, key = { it.slug }) { regla ->
                        BloqueMecanicaRetro(regla.nombre, regla.descripcion)
                    }
                }

                // Espaciador final
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun BloqueMecanicaRetro(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = titulo.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = FuentePixelFallback,
                fontWeight = FontWeight.Bold,
                fontSize = 11.sp,
                letterSpacing = 1.sp,
            ),
            color = Ambar400,
        )

        Text(
            text = descripcion,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 18.sp,
            ),
            color = Cream200,
        )
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
