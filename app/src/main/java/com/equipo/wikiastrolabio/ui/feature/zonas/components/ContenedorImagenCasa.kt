package com.equipo.wikiastrolabio.ui.feature.zonas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800

/**
 * Contenedor de la ilustración de la Casa según el prototipo de Figma:
 * - Recuadro técnico con borde Slate-800.
 * - Esquinas técnicas de mira/astrolabio (arriba-izq ┌ y abajo-der ┘).
 * - Ícono pixel art de imagen con "CASA DE ..." y "RECURSO OFICIAL PENDIENTE"
 *   cuando la imagen aún no está disponible.
 */
@Composable
fun ContenedorImagenCasa(
    nombreZona: String,
    imagenUrl: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9.5f)
            .background(Background, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp)),
    ) {
        if (imagenUrl != null) {
            ImagenPixelArt(
                url = imagenUrl,
                contentDescription = nombreZona,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            // Placeholder técnico de Figma
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Ícono pixel art de fotografía / imagen
                IconoFotoPixel(modifier = Modifier.size(28.dp))

                // Texto "CASA DE GÉMINIS"
                Text(
                    text = nombreZona.uppercase(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = PixelifySans,
                        fontSize = 10.sp,
                        letterSpacing = 1.2.sp,
                    ),
                    color = Sand300,
                )

                // Texto "RECURSO OFICIAL PENDIENTE"
                Text(
                    text = "RECURSO OFICIAL PENDIENTE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = PixelifySans,
                        fontSize = 8.5.sp,
                        letterSpacing = 0.8.sp,
                    ),
                    color = Slate700,
                )
            }
        }

        // Esquinas técnicas de astrolabio (arriba a la izquierda y abajo a la derecha)
        Canvas(modifier = Modifier.fillMaxSize()) {
            val len = 12.dp.toPx()
            val stroke = 1.5.dp.toPx()
            val pad = 8.dp.toPx()

            // Esquina arriba-izquierda ┌
            drawLine(Slate700, Offset(pad, pad), Offset(pad + len, pad), strokeWidth = stroke)
            drawLine(Slate700, Offset(pad, pad), Offset(pad, pad + len), strokeWidth = stroke)

            // Esquina abajo-derecha ┘
            val bx = size.width - pad
            val by = size.height - pad
            drawLine(Slate700, Offset(bx - len, by), Offset(bx, by), strokeWidth = stroke)
            drawLine(Slate700, Offset(bx, by - len), Offset(bx, by), strokeWidth = stroke)
        }
    }
}

/** Ícono pixel-art de fotografía/arte */
@Composable
private fun IconoFotoPixel(modifier: Modifier = Modifier) {
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

        // Paisaje dentro (montaña pixelada y sol)
        p(10, 4, Ambar400, 2, 2) // Sol / astro

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
