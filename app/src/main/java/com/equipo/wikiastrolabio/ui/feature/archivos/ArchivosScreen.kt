package com.equipo.wikiastrolabio.ui.feature.archivos

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonAtrasPixel
import com.equipo.wikiastrolabio.ui.components.ContenidoSegunEstado
import com.equipo.wikiastrolabio.ui.feature.archivos.components.AcordeonCategoria
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ArchivosRoute(
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    viewModel: ArchivosViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ArchivosScreen(
        uiState = uiState,
        onAtras = onAtras,
        onArchivoClick = onArchivoClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/**
 * Pantalla de Archivos / Códice rediseñada según el prototipo de Figma:
 * - Botón atrás retro BotonAtrasPixel (cuadrado con borde ámbar).
 * - Título principal "ARCHIVOS" en tipografía pixel/serif retro.
 * - Subtítulo "Vestigios de un tiempo que permanece.".
 * - Encabezado de sección con estrella pixel art: "★ COLECCIONES ──────────".
 * - Colecciones en tarjetas de acordeón con vista de documento técnico y registros.
 */
@Composable
fun ArchivosScreen(
    uiState: UiState<List<CategoriaArchivo>>,
    onAtras: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var abiertas by rememberSaveable { mutableStateOf<Set<String>?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
    ) {
        ContenidoSegunEstado(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
        ) { categorias ->
            // La primera colección empieza abierta como en Figma
            val expandidas = abiertas ?: setOfNotNull(categorias.firstOrNull()?.slug)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                // 1. Botón atrás retro cuadrado con borde ámbar
                item {
                    BotonAtrasPixel(onClick = onAtras)
                }

                // 2. Título principal "ARCHIVOS" y subtítulo
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "ARCHIVOS",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontFamily = FuentePixelTitulosFallback,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp,
                                fontSize = 26.sp,
                            ),
                            color = Blanco,
                        )

                        Text(
                            text = stringResource(R.string.archivos_subtitulo),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = FuentePixelFallback,
                                fontSize = 12.5.sp,
                                letterSpacing = 0.4.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }

                // 3. Encabezado "★ COLECCIONES ──────────"
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, bottom = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconoEstrellaPixel(modifier = Modifier.size(10.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "COLECCIONES",
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

                // 4. Lista de colecciones / acordeones
                itemsIndexed(categorias, key = { _, categoria -> categoria.slug }) { index, categoria ->
                    AcordeonCategoria(
                        categoria = categoria,
                        expandida = categoria.slug in expandidas,
                        indice = index + 1,
                        onAlternar = {
                            abiertas = if (categoria.slug in expandidas) {
                                expandidas - categoria.slug
                            } else {
                                expandidas + categoria.slug
                            }
                        },
                        onArchivoClick = onArchivoClick,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

/** Ícono de estrella pixel-art para el divisor de colecciones */
@Composable
private fun IconoEstrellaPixel(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 7f
        fun p(x: Int, y: Int) {
            drawRect(Sand300, Offset(x * px, y * px), Size(px, px))
        }
        // Estrella simétrica pixel art
        p(3, 0); p(3, 1); p(3, 2); p(3, 3); p(3, 4); p(3, 5); p(3, 6)
        p(0, 3); p(1, 3); p(2, 3); p(4, 3); p(5, 3); p(6, 3)
        p(2, 2); p(4, 2); p(2, 4); p(4, 4)
    }
}

@Preview(showBackground = true)
@Composable
private fun ArchivosScreenPreview() {
    WikiAstrolabioTheme {
        ArchivosScreen(
            uiState = UiState.Exito(DatosPreview.categoriasArchivo),
            onAtras = {},
            onArchivoClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
