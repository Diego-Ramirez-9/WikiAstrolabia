package com.equipo.wikiastrolabio.ui.feature.archivos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Tarjeta acordeón de colección de archivos rediseñada según Figma:
 * - Recuadro izquierdo punteado con ícono pixel art.
 * - Etiqueta "COLECCIÓN 01" y título en terracota ("FRAGMENTOS DE ESTRELLA", "POEMAS").
 * - Indicador de acordeón retro (+ o −).
 * - Al desplegarse muestra el contenedor técnico de documento y los registros oficiales.
 */
@Composable
fun AcordeonCategoria(
    categoria: CategoriaArchivo,
    expandida: Boolean,
    onAlternar: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
    indice: Int = 1,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Obsidian900, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
            .padding(16.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Cabecera interactiva del acordeón
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onAlternar),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // 1. Recuadro técnico con borde punteado para el ícono
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .background(Background, RoundedCornerShape(2.dp))
                        .drawBehind {
                            drawRoundRect(
                                color = Slate700,
                                style = Stroke(
                                    width = 1.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(3.dp.toPx(), 3.dp.toPx()), 0f),
                                ),
                                cornerRadius = CornerRadius(2.dp.toPx()),
                            )
                        },
                    contentAlignment = Alignment.Center,
                ) {
                    if (categoria.iconoUrl != null) {
                        ImagenPixelArt(
                            url = categoria.iconoUrl,
                            contentDescription = categoria.nombre,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(6.dp),
                        )
                    } else {
                        IconoFotoArchivoPixel(modifier = Modifier.size(24.dp))
                    }
                }

                // 2. Información de la colección (Etiqueta + Título)
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = "COLECCIÓN ${indice.toString().padStart(2, '0')}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FuentePixelFallback,
                            fontWeight = FontWeight.Bold,
                            fontSize = 9.5.sp,
                            letterSpacing = 1.2.sp,
                        ),
                        color = Sand300,
                    )

                    Text(
                        text = categoria.nombre.uppercase(),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FuentePixelTitulosFallback,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 17.sp,
                            letterSpacing = 1.2.sp,
                        ),
                        color = TerraCotta500,
                    )
                }

                // 3. Indicador de acordeón (+ / −)
                Text(
                    text = if (expandida) "−" else "+",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontFamily = FuentePixelTitulosFallback,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    ),
                    color = TerraCotta500,
                )
            }

            // Contenido desplegado
            AnimatedVisibility(visible = expandida) {
                Column(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    HorizontalDivider(thickness = 1.dp, color = Slate800)

                    // Contenedor técnico de Documento Pixel Art
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 8.5f)
                            .background(Background, RoundedCornerShape(2.dp))
                            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp)),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            IconoFotoArchivoPixel(modifier = Modifier.size(26.dp))

                            Text(
                                text = "DOCUMENTO PIXEL ART",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FuentePixelFallback,
                                    fontSize = 9.5.sp,
                                    letterSpacing = 1.2.sp,
                                ),
                                color = Sand300,
                            )

                            Text(
                                text = "RECURSO OFICIAL PENDIENTE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = FuentePixelFallback,
                                    fontSize = 8.sp,
                                    letterSpacing = 0.8.sp,
                                ),
                                color = Slate700,
                            )
                        }

                        // Esquinas técnicas de astrolabio (arriba-izq ┌ y abajo-der ┘)
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val len = 12.dp.toPx()
                            val stroke = 1.5.dp.toPx()
                            val pad = 8.dp.toPx()

                            drawLine(Slate700, Offset(pad, pad), Offset(pad + len, pad), strokeWidth = stroke)
                            drawLine(Slate700, Offset(pad, pad), Offset(pad, pad + len), strokeWidth = stroke)

                            val bx = size.width - pad
                            val by = size.height - pad
                            drawLine(Slate700, Offset(bx - len, by), Offset(bx, by), strokeWidth = stroke)
                            drawLine(Slate700, Offset(bx, by - len), Offset(bx, by), strokeWidth = stroke)
                        }
                    }

                    // Descripción de la colección
                    Text(
                        text = categoria.descripcion ?: "Espacio preparado para los registros oficiales de esta colección.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = FuentePixelFallback,
                            fontSize = 12.sp,
                            lineHeight = 17.sp,
                            letterSpacing = 0.3.sp,
                        ),
                        color = Cream200,
                    )

                    // Lista de registros disponibles en la colección
                    if (categoria.elementos.isNotEmpty()) {
                        Column(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            categoria.elementos.forEach { elemento ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Background, RoundedCornerShape(2.dp))
                                        .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
                                        .clickable { onArchivoClick(elemento.slug) }
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = elemento.titulo,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontFamily = FuentePixelTitulosFallback,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.5.sp,
                                            ),
                                            color = Cream200,
                                        )
                                        elemento.ubicacion?.let {
                                            Text(
                                                text = it,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontFamily = FuentePixelFallback,
                                                    fontSize = 10.sp,
                                                ),
                                                color = Sand300,
                                            )
                                        }
                                    }

                                    Icon(
                                        painter = painterResource(R.drawable.ic_flecha_derecha_pixel),
                                        contentDescription = null,
                                        tint = Ambar400,
                                        modifier = Modifier.size(12.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/** Ícono pixel-art para los archivos */
@Composable
private fun IconoFotoArchivoPixel(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val px = size.width / 16f
        fun p(x: Int, y: Int, color: Color, w: Int = 1, h: Int = 1) {
            drawRect(color, Offset(x * px, y * px), Size(w * px, h * px))
        }

        // Marco exterior
        p(2, 2, Cream200, 12, 1)
        p(2, 13, Cream200, 12, 1)
        p(2, 3, Cream200, 1, 10)
        p(13, 3, Cream200, 1, 10)

        // Sol / astro
        p(10, 4, Ambar400, 2, 2)

        // Colina izquierda
        p(4, 11, Sand300, 2, 2)
        p(5, 9, Sand300, 2, 2)
        p(6, 8, Sand300, 2, 2)

        // Colina derecha
        p(7, 9, Slate700, 2, 4)
        p(9, 7, Slate700, 2, 6)
        p(11, 10, Slate700, 2, 3)
    }
}

@Preview(showBackground = true)
@Composable
private fun AcordeonCategoriaPreview() {
    WikiAstrolabioTheme {
        AcordeonCategoria(
            categoria = DatosPreview.categoriasArchivo.first(),
            expandida = true,
            onAlternar = {},
            onArchivoClick = {},
            indice = 1,
        )
    }
}
