package com.equipo.wikiastrolabio.ui.feature.mundo

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.feature.mundo.components.MapaGranReloj
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
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
 * Pantalla de Mundo / El Gran Reloj rediseñada según el prototipo de Figma:
 * - Botón atrás pixel-art en recuadro cuadrado
 * - Título "EL GRAN RELOJ" con subtítulo
 * - Mapa técnico del cosmos con marcadores 01 a 06
 * - Instrucción "TOCA UN MARCADOR PARA EXPLORAR"
 * - Tarjeta inferior de localización con acción "EXPLORAR LA CASA" (habilitada solo para Casas)
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
    // Marcador seleccionado: por defecto "geminis" (03) como en el mockup
    var slugSeleccionado by rememberSaveable { mutableStateOf("geminis") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
    ) {
        ContenidoSegunEstado(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
        ) { mapa ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                // 1. Botón atrás retro pixel en cuadrado con borde ámbar
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "EL GRAN RELOJ" y subtítulo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "EL GRAN RELOJ",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = PixelifySans,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 26.sp,
                            ),
                            color = Blanco,
                        )
                        Text(
                            text = "Seis lugares. Un mismo mecanismo.",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = PixelifySans,
                                fontSize = 12.5.sp,
                                letterSpacing = 0.3.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }

                // 3. Encabezado de sección "MAPA DEL COSMOS"
                item {
                    Text(
                        text = stringResource(R.string.mundo_mapa).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PixelifySans,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp,
                            fontSize = 10.sp,
                        ),
                        color = Cream200,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }

                // 4. El Mapa Cósmico Interactivo
                item {
                    MapaGranReloj(
                        mapa = mapa,
                        slugSeleccionado = slugSeleccionado,
                        onPuntoClick = { punto -> slugSeleccionado = punto.slug },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                // 5. Texto de instrucción centrado
                item {
                    Text(
                        text = "TOCA UN MARCADOR PARA EXPLORAR",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PixelifySans,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.2.sp,
                            fontSize = 10.sp,
                        ),
                        color = Cream200.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                    )
                }

                // 6. Tarjeta inferior de localización seleccionada
                val seleccionado = mapa.puntos.firstOrNull { it.slug == slugSeleccionado }
                    ?: PuntoMapa(
                        slug = slugSeleccionado,
                        nombre = "Casa de Géminis",
                        subtitulo = "La Casa de los Reflejos",
                        tipo = TipoZona.CASA,
                        resumen = null,
                        x = 0.22f,
                        y = 0.50f,
                    )

                val numeroLoc = when (seleccionado.slug) {
                    "aries" -> "01"
                    "entranas" -> "02"
                    "geminis" -> "03"
                    "acuario" -> "04"
                    "casa-05" -> "05"
                    "camara-central" -> "06"
                    else -> "03"
                }

                item {
                    TarjetaLocalizacionFigma(
                        numero = numeroLoc,
                        punto = seleccionado,
                        onExplorar = { onExplorarZona(seleccionado.slug) },
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

/**
 * Tarjeta de localización según el prototipo de Figma:
 * - "LOCALIZACIÓN XX"
 * - Nombre de la Casa / Zona en Terracota
 * - Línea divisoria Slate-700
 * - Acción "EXPLORAR LA CASA ➔" (solo activa si es una Casa del zodiaco)
 */
@Composable
private fun TarjetaLocalizacionFigma(
    numero: String,
    punto: PuntoMapa,
    onExplorar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val esCasa = punto.tipo == TipoZona.CASA

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(4.dp),
        color = Obsidian900,
        border = BorderStroke(1.dp, Slate800),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // "LOCALIZACIÓN 03"
            Text(
                text = "LOCALIZACIÓN $numero",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = PixelifySans,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 10.sp,
                ),
                color = Sand300,
            )

            // Nombre de la Casa / Zona
            Text(
                text = punto.nombre.uppercase(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = PixelifySans,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    fontSize = 20.sp,
                ),
                color = TerraCotta500,
            )

            HorizontalDivider(thickness = 1.dp, color = Slate700)

            // Fila inferior de acción: solo explorable si es CASA
            if (esCasa) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onExplorar)
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "EXPLORAR LA CASA",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PixelifySans,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            fontSize = 11.sp,
                        ),
                        color = Cream200,
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_flecha_derecha_pixel),
                        contentDescription = "Explorar",
                        tint = Ambar400,
                        modifier = Modifier.size(18.dp),
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "ZONA CERRADA · SOLO CASAS EXPLORABLES",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PixelifySans,
                            fontSize = 9.5.sp,
                            letterSpacing = 0.8.sp,
                        ),
                        color = Slate700,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B151E)
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
