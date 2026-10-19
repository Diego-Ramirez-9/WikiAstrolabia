package com.equipo.wikiastrolabio.ui.feature.mecanicas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.domain.model.Habilidad
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
 * Tarjeta de habilidad de Almanaque XII rediseñada según el prototipo de Figma:
 * - Recuadro izquierdo punteado para el ícono pixel-art + "ÍCONO".
 * - Nombre de habilidad en terracota ("SALTO", "ANCLA", "GANCHO", "ESCALAR") + número "01".
 * - Descripción concisa en tipografía retro pixel/monospace.
 * - Chip técnico inferior ("MOVIMIENTO", "≈ 5 SEGUNDOS", "2 USOS", "≈ 3 SEGUNDOS").
 */
@Composable
fun TarjetaHabilidad(
    habilidad: Habilidad,
    modifier: Modifier = Modifier,
) {
    val tituloMostrado = when (habilidad.slug) {
        "salto" -> "SALTO"
        "punto-de-anclaje" -> "ANCLA"
        "gancho" -> "GANCHO"
        "agarre-y-escalada" -> "ESCALAR"
        else -> habilidad.nombreCorto.uppercase()
    }

    val descripcionMostrada = when (habilidad.slug) {
        "salto" -> "Movimiento básico."
        "punto-de-anclaje" -> "Fija un punto temporal durante aproximadamente 5 segundos."
        "gancho" -> "Tiene 2 usos y se recarga al tocar suelo firme."
        "agarre-y-escalada" -> "Permite subir determinadas superficies durante aproximadamente 3 segundos."
        else -> habilidad.descripcion
    }

    val etiquetaMostrada = when (habilidad.slug) {
        "salto" -> "MOVIMIENTO"
        "punto-de-anclaje" -> "≈ 5 SEGUNDOS"
        "gancho" -> "2 USOS"
        "agarre-y-escalada" -> "≈ 3 SEGUNDOS"
        else -> habilidad.etiqueta?.uppercase()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Obsidian900, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top,
        ) {
            // 1. Recuadro izquierdo técnico con borde punteado para el ícono
            Box(
                modifier = Modifier
                    .size(64.dp)
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
                if (habilidad.iconoUrl != null) {
                    ImagenPixelArt(
                        url = habilidad.iconoUrl,
                        contentDescription = habilidad.nombreCorto,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp),
                    )
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterVertically),
                    ) {
                        IconoMecanicaPixel(modifier = Modifier.size(24.dp))
                        Text(
                            text = "ÍCONO",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FuentePixelFallback,
                                fontSize = 7.5.sp,
                                letterSpacing = 0.8.sp,
                            ),
                            color = Slate700,
                        )
                    }
                }
            }

            // 2. Información de la mecánica (Título, número, descripción, chip)
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Título y número (ej. "SALTO  01")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = tituloMostrado,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FuentePixelTitulosFallback,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            letterSpacing = 1.2.sp,
                        ),
                        color = TerraCotta500,
                    )

                    Text(
                        text = habilidad.numero.toString().padStart(2, '0'),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = FuentePixelFallback,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            letterSpacing = 1.sp,
                        ),
                        color = Sand300,
                    )
                }

                // Descripción
                Text(
                    text = descripcionMostrada,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FuentePixelFallback,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        letterSpacing = 0.3.sp,
                    ),
                    color = Cream200,
                )

                // Chip / Badge inferior
                if (etiquetaMostrada != null) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF0F1E2A), RoundedCornerShape(2.dp))
                            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = etiquetaMostrada,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FuentePixelFallback,
                                fontWeight = FontWeight.Normal,
                                fontSize = 9.sp,
                                letterSpacing = 1.sp,
                            ),
                            color = Sand300,
                        )
                    }
                }
            }
        }
    }
}

/** Ícono pixel-art genérico para las mecánicas */
@Composable
private fun IconoMecanicaPixel(modifier: Modifier = Modifier) {
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
private fun TarjetaHabilidadPreview() {
    WikiAstrolabioTheme {
        TarjetaHabilidad(habilidad = DatosPreview.mecanicas.habilidades.first())
    }
}
