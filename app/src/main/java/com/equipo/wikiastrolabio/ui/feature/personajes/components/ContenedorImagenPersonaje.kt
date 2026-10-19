package com.equipo.wikiastrolabio.ui.feature.personajes.components

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
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800

/**
 * Contenedor técnico de la ilustración del personaje según el prototipo de Figma:
 * - Borde Slate-800 sobre fondo Background.
 * - Esquinas técnicas de astrolabio (arriba-izq ┌ y abajo-der ┘).
 * - Ícono pixel-art de foto + "ILUSTRACIÓN DEL PERSONAJE" + "RECURSO OFICIAL PENDIENTE".
 */
@Composable
fun ContenedorImagenPersonaje(
    nombrePersonaje: String,
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
                contentDescription = nombrePersonaje,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconoFotoPixel(modifier = Modifier.size(28.dp))

                Text(
                    text = "ILUSTRACIÓN DEL PERSONAJE",
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
        }

        // Esquinas técnicas de astrolabio (arriba-izq ┌ y abajo-der ┘)
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

/** Ícono pixel-art de fotografía */
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
