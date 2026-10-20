package com.equipo.wikiastrolabio.ui.components.pixel

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Crema100
import com.equipo.wikiastrolabio.ui.theme.Noche700
import com.equipo.wikiastrolabio.ui.theme.Noche800
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * Cielo estrellado en pixel art con una nebulosa del color de [acento]. Se usa como fondo de
 * las ilustraciones que todavía no existen: [semilla] (ej. el slug) hace que cada elemento
 * tenga siempre el mismo cielo, distinto al de los demás.
 */
@Composable
fun CampoEstelar(semilla: String, acento: Color, modifier: Modifier = Modifier) {
    val estrellas = remember(semilla) {
        val azar = Random(semilla.hashCode())
        List(46) { Triple(azar.nextFloat(), azar.nextFloat(), azar.nextInt(10)) }
    }
    val centroNebulosa = remember(semilla) {
        val azar = Random(semilla.hashCode() xor 0x5f3759df)
        Offset(0.2f + azar.nextFloat() * 0.6f, 0.25f + azar.nextFloat() * 0.5f)
    }
    Canvas(modifier = modifier) {
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(acento.copy(alpha = 0.22f), acento.copy(alpha = 0.06f), Color.Transparent),
                center = Offset(size.width * centroNebulosa.x, size.height * centroNebulosa.y),
                radius = size.maxDimension * 0.55f,
            ),
        )
        val pixel = 2.dp.toPx()
        estrellas.forEach { (x, y, brillo) ->
            val color = when {
                brillo >= 9 -> Crema100
                brillo >= 7 -> acento.copy(alpha = 0.8f)
                else -> Crema100.copy(alpha = 0.18f + brillo * 0.05f)
            }
            val tamano = if (brillo >= 9) pixel * 2 else pixel
            // Se alinean a la cuadrícula de píxeles para que no se vean difuminadas.
            val px = (x * size.width / pixel).toInt() * pixel
            val py = (y * size.height / pixel).toInt() * pixel
            drawRect(color, Offset(px, py), Size(tamano, tamano))
            if (brillo >= 9) {
                // Destello en cruz de las estrellas más brillantes.
                drawRect(color.copy(alpha = 0.5f), Offset(px - pixel, py + pixel / 2), Size(tamano + pixel * 2, pixel))
                drawRect(color.copy(alpha = 0.5f), Offset(px + pixel / 2, py - pixel), Size(pixel, tamano + pixel * 2))
            }
        }
    }
}

/** Desvanece el contenido hacia abajo (a partir de [desde], de 0 a 1) para que no corte en seco. */
fun Modifier.desvanecerAbajo(desde: Float = 0.55f): Modifier = this
    .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        drawRect(
            brush = Brush.verticalGradient(desde to Color.Black, 1f to Color.Transparent),
            blendMode = BlendMode.DstIn,
        )
    }

/** Escuadras de lente en las cuatro esquinas, como en los marcos del Figma. */
fun Modifier.esquinasTecnicas(color: Color, largo: Dp = 14.dp, margen: Dp = 8.dp): Modifier = drawWithContent {
    drawContent()
    val l = largo.toPx()
    val m = margen.toPx()
    val g = 1.5.dp.toPx()
    val w = size.width
    val h = size.height
    // Superior izquierda y superior derecha.
    drawRect(color, Offset(m, m), Size(l, g)); drawRect(color, Offset(m, m), Size(g, l))
    drawRect(color, Offset(w - m - l, m), Size(l, g)); drawRect(color, Offset(w - m - g, m), Size(g, l))
    // Inferior izquierda e inferior derecha.
    drawRect(color, Offset(m, h - m - g), Size(l, g)); drawRect(color, Offset(m, h - m - l), Size(g, l))
    drawRect(color, Offset(w - m - l, h - m - g), Size(l, g)); drawRect(color, Offset(w - m - g, h - m - l), Size(g, l))
}

/**
 * Plano técnico del Gran Reloj (diseño del equipo de UI/UX): cuadrante punteado con marcas
 * horarias, manecillas y marco escalonado. Decora el encabezado de Inicio.
 */
@Composable
fun RelojCosmicoDecorativo(
    modifier: Modifier = Modifier,
    tamano: Dp = 150.dp,
    colorLineas: Color = Noche700,
    colorPivote: Color = Ambar400,
) {
    Canvas(modifier = modifier.size(tamano)) {
        val w = size.width
        val h = size.height
        val cx = w * 0.62f
        val cy = h * 0.42f
        val r = w * 0.34f
        val fino = Stroke(width = 1.2.dp.toPx() / 1.5f)
        val punteado = Stroke(width = 1.dp.toPx(), pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f))

        drawCircle(colorLineas, radius = r, center = Offset(cx, cy), style = punteado)
        drawCircle(Noche800, radius = r * 0.62f, center = Offset(cx, cy), style = fino)
        drawCircle(Noche800, radius = r * 1.22f, center = Offset(cx, cy), style = fino)

        for (i in 0 until 12) {
            val angulo = (i * 30.0 * PI / 180.0).toFloat()
            val principal = i % 3 == 0
            val desde = r - if (principal) 8f else 5f
            val hasta = r + 4f
            drawLine(
                color = if (principal) colorPivote.copy(alpha = 0.7f) else colorLineas,
                start = Offset(cx + cos(angulo) * desde, cy + sin(angulo) * desde),
                end = Offset(cx + cos(angulo) * hasta, cy + sin(angulo) * hasta),
                strokeWidth = if (principal) 2.2f else 1.2f,
            )
        }

        // Manecillas: una hacia las 12 y otra hacia las 4.
        drawLine(colorLineas, Offset(cx, cy - r * 0.85f), Offset(cx, cy), strokeWidth = 2f)
        val angulo = (130.0 * PI / 180.0).toFloat()
        drawLine(colorPivote.copy(alpha = 0.8f), Offset(cx, cy), Offset(cx + cos(angulo) * r * 0.7f, cy + sin(angulo) * r * 0.7f), strokeWidth = 2.4f)
        drawCircle(colorPivote, radius = 4f, center = Offset(cx, cy))
        drawCircle(colorLineas, radius = 8f, center = Offset(cx, cy), style = fino)
    }
}
