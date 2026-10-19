package com.equipo.wikiastrolabio.ui.feature.zonas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.feature.zonas.components.ContenedorImagenCasa
import com.equipo.wikiastrolabio.ui.feature.zonas.components.FilaSala
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
fun ZonaRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: ZonaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pestanas by viewModel.pestanas.collectAsStateWithLifecycle()
    val slugSeleccionado by viewModel.slugSeleccionado.collectAsStateWithLifecycle()
    ZonaScreen(
        uiState = uiState,
        pestanas = pestanas,
        slugSeleccionado = slugSeleccionado,
        onPestanaClick = viewModel::seleccionarZona,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/**
 * Pantalla de Casas / Zonas rediseñada según el prototipo de Figma:
 * - Botón atrás retro en cuadrado con borde ámbar
 * - Título principal "ZONAS" en tipografía pixel/serif retro
 * - Barra de selección de Casas dividida en mitades iguales (GÉMINIS | ACUARIO)
 * - Contenedor técnico de imagen con esquinas de astrolabio
 * - Título de la Casa en color terracota ("CASA DE LOS REFLEJOS", "CASA DE LAS CORRIENTES")
 * - Chips técnicos de etiquetas con borde Slate-800
 * - Encabezado "HABITACIONES / 06 ──────────"
 * - Lista de salas con número y nombre en estilo retro
 * - Registro y mecánicas de la Casa disponibles al desplazar hacia abajo
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ZonaScreen(
    uiState: UiState<Zona>,
    pestanas: List<ZonaResumen>,
    slugSeleccionado: String,
    onPestanaClick: (slug: String) -> Unit,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
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
        ) { zona ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // 1. Botón atrás retro cuadrado con borde ámbar
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "ZONAS"
                item {
                    Text(
                        text = "ZONAS",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = FuentePixelTitulosFallback,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            fontSize = 26.sp,
                        ),
                        color = Blanco,
                    )
                }

                // 3. Barra de pestañas de las Casas (GÉMINIS | ACUARIO)
                if (pestanas.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp),
                        ) {
                            pestanas.forEach { casa ->
                                val activa = casa.slug == slugSeleccionado
                                val nombreTab = casa.nombre
                                    .replaceFirst(Regex("^(Casa de|Casa)\\s+", RegexOption.IGNORE_CASE), "")
                                    .trim()
                                    .uppercase()

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .background(if (activa) Ambar400 else Color(0xFF754B2A))
                                        .clickable { onPestanaClick(casa.slug) },
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = nombreTab,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = FuentePixelFallback,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            letterSpacing = 1.2.sp,
                                        ),
                                        color = if (activa) Blanco else Color(0xFFC0A080),
                                    )
                                }
                            }
                        }
                    }
                }

                // 4. Ilustración técnica de la Casa
                item {
                    ContenedorImagenCasa(
                        nombreZona = zona.nombre,
                        imagenUrl = zona.imagenUrl,
                    )
                }

                // 5. Subtítulo de la Casa (ej. "CASA DE LOS REFLEJOS" o "CASA DE LAS CORRIENTES")
                item {
                    val subtituloTexto = (zona.subtitulo ?: zona.nombre)
                        .replaceFirst(Regex("^(La|El)\\s+", RegexOption.IGNORE_CASE), "")
                        .trim()
                        .uppercase()

                    Text(
                        text = subtituloTexto,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = FuentePixelTitulosFallback,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            letterSpacing = 1.2.sp,
                        ),
                        color = TerraCotta500,
                    )
                }

                // 6. Etiquetas / Chips técnicos
                if (zona.etiquetas.isNotEmpty()) {
                    item {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            zona.etiquetas.forEach { etiqueta ->
                                Box(
                                    modifier = Modifier
                                        .background(Obsidian900, RoundedCornerShape(2.dp))
                                        .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
                                        .padding(horizontal = 12.dp, vertical = 7.dp),
                                 ) {
                                    Text(
                                        text = etiqueta,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontFamily = FuentePixelFallback,
                                            fontWeight = FontWeight.Normal,
                                            fontSize = 11.sp,
                                            letterSpacing = 0.5.sp,
                                        ),
                                        color = Cream200,
                                    )
                                }
                            }
                        }
                    }
                }

                // 7. Encabezado de habitaciones: "HABITACIONES / 06 ──────────"
                if (zona.salas.isNotEmpty()) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 2.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "HABITACIONES / " + zona.salas.size.toString().padStart(2, '0'),
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

                    // 8. Lista de habitaciones
                    items(zona.salas, key = { it.slug }) { sala ->
                        FilaSala(sala = sala, onClick = { onSalaClick(sala.slug) })
                    }
                }

                // 9. Registro / Lore de la Casa
                val tieneRegistro = !zona.resumen.isNullOrBlank() || !zona.narrativa.isNullOrBlank() ||
                    !zona.exterior.isNullOrBlank() || !zona.ambientacion.isNullOrBlank() ||
                    !zona.restauracion.isNullOrBlank()

                if (tieneRegistro) {
                    item {
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                             verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "REGISTRO DE LA CASA",
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

                    zona.resumen?.let { item { BloqueTextoRetro("Resumen", it) } }
                    zona.exterior?.let { item { BloqueTextoRetro("Estructura Exterior", it) } }
                    zona.ambientacion?.let { item { BloqueTextoRetro("Atmósfera y Entorno", it) } }
                    zona.narrativa?.let { item { BloqueTextoRetro("Transmisión Holográfica", it) } }
                    zona.restauracion?.let { item { BloqueTextoRetro("Protocolo de Restauración", it) } }
                }

                // 10. Mecánicas de la Casa
                if (zona.mecanicas.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                text = "MECÁNICAS",
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

                    items(zona.mecanicas, key = { it.slug }) { mecanica ->
                        BloqueTextoRetro(mecanica.nombre, mecanica.descripcion)
                    }
                }
            }
        }
    }
}

@Composable
private fun BloqueTextoRetro(
    titulo: String,
    texto: String,
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
            text = texto,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                lineHeight = 19.sp,
            ),
            color = Cream200,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ZonaScreenPreview() {
    WikiAstrolabioTheme {
        ZonaScreen(
            uiState = UiState.Exito(DatosPreview.zona),
            pestanas = DatosPreview.casas,
            slugSeleccionado = DatosPreview.zona.slug,
            onPestanaClick = {},
            onAtras = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
