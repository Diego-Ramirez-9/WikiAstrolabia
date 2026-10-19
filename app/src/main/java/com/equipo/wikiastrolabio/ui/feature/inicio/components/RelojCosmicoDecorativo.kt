package com.equipo.wikiastrolabio.ui.feature.inicio.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Gráfico técnico decorativo del Gran Reloj Cósmico / Astrolabio
 * según el diseño de Figma:
 * - Marco perimetral técnico escalonado (blueprint).
 * - Cuadrante circular punteado con marcas astronómicas.
 * - Eje central con manecillas mecánicas (vertical y diagonal hacia 4:30).
 * - Marcas cardinales con retículas técnicas a las 6 y a las 9.
 */
@Composable
fun RelojCosmicoDecorativo(
    modifier: Modifier = Modifier,
    tamano: Dp = 150.dp,
    colorLineas: Color = Slate700,
) {
    Canvas(modifier = modifier.size(tamano)) {
        val w = size.width
        val h = size.height

        // Centro del reloj en la composición (desplazado hacia la derecha/arriba)
        val cx = w * 0.65f
        val cy = h * 0.38f
        val r = w * 0.34f

        val strokeFino = Stroke(width = 1.2f)
        val strokeMedio = Stroke(width = 1.6f)
        val strokePunteado = Stroke(
            width = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f),
        )

        // 1. Marco técnico exterior escalonado (blueprint)
        val marcoBlueprint = Path().apply {
            // Comienza arriba a la izquierda del reloj
            moveTo(w * 0.22f, 0f)
            lineTo(w * 0.22f, h * 0.22f)
            lineTo(w * 0.35f, h * 0.22f)
            lineTo(w * 0.35f, h * 0.65f)
            lineTo(w * 0.42f, h * 0.65f)
            lineTo(w * 0.42f, h * 0.82f)
            lineTo(w * 0.58f, h * 0.82f)
            lineTo(w * 0.58f, h * 0.94f)
            lineTo(w * 0.85f, h * 0.94f)
            lineTo(w * 0.85f, h * 0.78f)
            lineTo(w, h * 0.78f)
        }
        drawPath(
            path = marcoBlueprint,
            color = Slate800,
            style = strokeFino,
        )

        // 2. Círculo principal del reloj (punteado técnico)
        drawCircle(
            color = colorLineas,
            radius = r,
            center = Offset(cx, cy),
            style = strokePunteado,
        )

        // 3. Círculo concéntrico interior fino
        drawCircle(
            color = Slate800,
            radius = r * 0.62f,
            center = Offset(cx, cy),
            style = strokeFino,
        )

        // 4. Marcas perimetrales (ticks astronómicos cada 30 grados)
        for (i in 0 until 12) {
            val angulo = (i * 30.0 * PI / 180.0).toFloat()
            val rIn = r - 5f
            val rOut = r + 4f
            val x1 = cx + cos(angulo) * rIn
            val y1 = cy + sin(angulo) * rIn
            val x2 = cx + cos(angulo) * rOut
            val y2 = cy + sin(angulo) * rOut

            drawLine(
                color = if (i % 3 == 0) colorLineas else Slate800,
                start = Offset(x1, y1),
                end = Offset(x2, y2),
                strokeWidth = if (i % 3 == 0) 1.8f else 1.2f,
            )
        }

        // 5. Retícula técnica cardinal a las 9 (izquierda)
        val x9 = cx - r
        val y9 = cy
        drawLine(
            color = colorLineas,
            start = Offset(x9 - 14f, y9),
            end = Offset(x9 + 8f, y9),
            strokeWidth = 1.5f,
        )
        drawLine(
            color = colorLineas,
            start = Offset(x9 - 6f, y9 - 4f),
            end = Offset(x9 - 6f, y9 + 4f),
            strokeWidth = 1.2f,
        )

        // 6. Retícula técnica cardinal a las 6 (abajo)
        val x6 = cx
        val y6 = cy + r
        drawLine(
            color = colorLineas,
            start = Offset(x6, y6 - 8f),
            end = Offset(x6, y6 + 14f),
            strokeWidth = 1.5f,
        )
        drawLine(
            color = colorLineas,
            start = Offset(x6 - 4f, y6 + 6f),
            end = Offset(x6 + 4f, y6 + 6f),
            strokeWidth = 1.2f,
        )

        // 7. Retícula técnica cardinal a las 3 (derecha)
        val x3 = cx + r
        val y3 = cy
        drawLine(
            color = colorLineas,
            start = Offset(x3 - 6f, y3),
            end = Offset(w, y3),
            strokeWidth = 1.2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(3f, 3f), 0f),
        )

        // 8. Manecillas del Reloj
        // Manecilla vertical (desde arriba hacia el centro)
        drawLine(
            color = colorLineas,
            start = Offset(cx, 0f),
            end = Offset(cx, cy),
            strokeWidth = 1.6f,
        )

        // Manecilla angular diagonal (hacia ~4:30 / 135 grados)
        val anguloManecilla = (130.0 * PI / 180.0).toFloat()
        val manoX = cx + cos(anguloManecilla) * (r * 0.9f)
        val manoY = cy + sin(anguloManecilla) * (r * 0.9f)
        drawLine(
            color = colorLineas,
            start = Offset(cx, cy),
            end = Offset(manoX, manoY),
            strokeWidth = 2.0f,
        )

        // 9. Pivote central del mecanismo
        drawCircle(
            color = Ambar400,
            radius = 3.5f,
            center = Offset(cx, cy),
        )
        drawCircle(
            color = Slate800,
            radius = 6.5f,
            center = Offset(cx, cy),
            style = strokeFino,
        )
    }
}
