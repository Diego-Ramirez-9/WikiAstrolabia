package com.equipo.wikiastrolabio.ui.feature.mundo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.MutedPurple600
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.Slate800

private val TamanoMarcador = 46.dp

/**
 * Mapa del Gran Reloj Cósmico rediseñado fielmente según el prototipo de Figma:
 * - Marco perimetral escalonado pixel art con esquinas técnicas de astrolabio.
 * - Guías de coordenadas concéntricas y ejes punteados.
 * - 6 marcadores con estrellas pixel art doradas/grises y numeración 01-06.
 * - Leyenda "GRAN RELOJ CÓSMICO" en la esquina inferior izquierda.
 */
@Composable
fun MapaGranReloj(
    mapa: Mapa,
    slugSeleccionado: String?,
    onPuntoClick: (PuntoMapa) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Definición de las 6 posiciones fijas del Gran Reloj (en coordenadas relativas 0f..1f)
    // 01: Superior, 02: Inferior, 03: Izquierda (Géminis), 04: Derecha (Acuario), 05: Superior derecha, 06: Centro
    val posiciones = listOf(
        CoordenadaMarcador(numero = "01", x = 0.50f, y = 0.16f, slugFallback = "aries", esCentro = false),
        CoordenadaMarcador(numero = "02", x = 0.50f, y = 0.83f, slugFallback = "entranas", esCentro = false),
        CoordenadaMarcador(numero = "03", x = 0.22f, y = 0.50f, slugFallback = "geminis", esCentro = false),
        CoordenadaMarcador(numero = "04", x = 0.78f, y = 0.50f, slugFallback = "acuario", esCentro = false),
        CoordenadaMarcador(numero = "05", x = 0.72f, y = 0.28f, slugFallback = "casa-05", esCentro = false),
        CoordenadaMarcador(numero = "06", x = 0.50f, y = 0.50f, slugFallback = "camara-central", esCentro = true),
    )

    Box(
        modifier = modifier
            .aspectRatio(0.96f)
            .background(Background, RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(4.dp)),
    ) {
        // Fondo técnico del Gran Reloj: perímetro escalonado y cuadrícula astronómica
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val cx = w * 0.5f
            val cy = h * 0.5f

            val strokeSolido = Stroke(width = 1.2f)
            val strokePunteado = Stroke(
                width = 1.2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f),
            )

            // 1. Ejes principales de coordenadas (cruz punteada)
            drawLine(Slate700, Offset(w * 0.18f, cy), Offset(w * 0.82f, cy), strokeWidth = 1f, pathEffect = strokePunteado.pathEffect)
            drawLine(Slate700, Offset(cx, h * 0.14f), Offset(cx, h * 0.86f), strokeWidth = 1f, pathEffect = strokePunteado.pathEffect)

            // 2. Cuadrícula concéntrica exterior (cuadrado punteado)
            val tamCuadradoExt = w * 0.62f
            drawRect(
                color = Slate700,
                topLeft = Offset(cx - tamCuadradoExt / 2, cy - tamCuadradoExt / 2),
                size = androidx.compose.ui.geometry.Size(tamCuadradoExt, tamCuadradoExt),
                style = strokePunteado,
            )

            // 3. Cuadrícula concéntrica interior (alrededor del centro 06)
            val tamCuadradoInt = w * 0.36f
            drawRect(
                color = Slate700,
                topLeft = Offset(cx - tamCuadradoInt / 2, cy - tamCuadradoInt / 2),
                size = androidx.compose.ui.geometry.Size(tamCuadradoInt, tamCuadradoInt),
                style = strokePunteado,
            )

            // 4. Perímetro escalonado cósmico pixel art (forma de astrolabio)
            val perimetro = Path().apply {
                // Arriba
                moveTo(cx - w * 0.16f, h * 0.08f)
                lineTo(cx + w * 0.16f, h * 0.08f)
                lineTo(cx + w * 0.16f, h * 0.12f)
                lineTo(cx + w * 0.28f, h * 0.12f)
                lineTo(cx + w * 0.28f, h * 0.18f)
                lineTo(cx + w * 0.38f, h * 0.18f)
                // Derecha
                lineTo(cx + w * 0.38f, cy - h * 0.16f)
                lineTo(cx + w * 0.44f, cy - h * 0.16f)
                lineTo(cx + w * 0.44f, cy + h * 0.16f)
                lineTo(cx + w * 0.38f, cy + h * 0.16f)
                lineTo(cx + w * 0.38f, cy + h * 0.28f)
                lineTo(cx + w * 0.28f, cy + h * 0.28f)
                // Abajo
                lineTo(cx + w * 0.28f, h * 0.88f)
                lineTo(cx + w * 0.16f, h * 0.88f)
                lineTo(cx + w * 0.16f, h * 0.92f)
                lineTo(cx - w * 0.16f, h * 0.92f)
                lineTo(cx - w * 0.16f, h * 0.88f)
                lineTo(cx - w * 0.28f, h * 0.88f)
                lineTo(cx - w * 0.28f, cy + h * 0.28f)
                lineTo(cx - w * 0.38f, cy + h * 0.28f)
                // Izquierda
                lineTo(cx - w * 0.38f, cy + h * 0.16f)
                lineTo(cx - w * 0.44f, cy + h * 0.16f)
                lineTo(cx - w * 0.44f, cy - h * 0.16f)
                lineTo(cx - w * 0.38f, cy - h * 0.16f)
                lineTo(cx - w * 0.38f, h * 0.18f)
                lineTo(cx - w * 0.28f, h * 0.18f)
                lineTo(cx - w * 0.28f, h * 0.12f)
                lineTo(cx - w * 0.16f, h * 0.12f)
                close()
            }
            drawPath(perimetro, color = Slate800, style = strokeSolido)
        }

        // 5. Marcadores 01 a 06 posicionados
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            posiciones.forEach { pos ->
                // Buscar si existe un PuntoMapa asociado en los datos reales del mapa
                val punto = mapa.puntos.firstOrNull { it.slug == pos.slugFallback }
                    ?: mapa.puntos.firstOrNull {
                        when (pos.numero) {
                            "02" -> it.tipo == TipoZona.EJE
                            "03" -> it.slug.contains("geminis")
                            "04" -> it.slug.contains("acuario")
                            "06" -> it.tipo == TipoZona.CIERRE
                            else -> false
                        }
                    }
                    ?: PuntoMapa(
                        slug = pos.slugFallback,
                        nombre = when (pos.numero) {
                            "01" -> "Casa de Aries"
                            "02" -> "Las Entrañas del Reloj"
                            "03" -> "Casa de Géminis"
                            "04" -> "Casa de Acuario"
                            "05" -> "Casa de Tauro"
                            else -> "La Cámara Central"
                        },
                        subtitulo = if (pos.numero in listOf("01", "03", "04", "05")) "Casa del Zodiaco" else "Zona del Reloj",
                        tipo = if (pos.numero in listOf("01", "03", "04", "05")) TipoZona.CASA else if (pos.numero == "02") TipoZona.EJE else TipoZona.CIERRE,
                        resumen = null,
                        x = pos.x,
                        y = pos.y,
                    )

                val seleccionado = punto.slug == slugSeleccionado || (slugSeleccionado == null && pos.numero == "03")

                MarcadorFigma(
                    numero = pos.numero,
                    seleccionado = seleccionado,
                    esCentro = pos.esCentro,
                    onClick = { onPuntoClick(punto) },
                    modifier = Modifier.offset(
                        x = maxWidth * pos.x - TamanoMarcador / 2,
                        y = maxHeight * pos.y - TamanoMarcador / 2,
                    ),
                )
            }
        }

        // 6. Leyenda "GRAN RELOJ CÓSMICO" abajo a la izquierda
        Text(
            text = "GRAN RELOJ CÓSMICO",
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = PixelifySans,
                fontSize = 9.sp,
                letterSpacing = 1.sp,
            ),
            color = Cream200.copy(alpha = 0.8f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 10.dp),
        )
    }
}

private data class CoordenadaMarcador(
    val numero: String,
    val x: Float,
    val y: Float,
    val slugFallback: String,
    val esCentro: Boolean,
)

/**
 * Marcador visual individual según el diseño de Figma:
 * - Recuadro cuadrado con fondo oscuro o violeta/ámbar al seleccionarse.
 * - Estrella dorada o gris pixel art (o reloj si es el centro).
 * - Número "01"-"06" en la esquina inferior derecha.
 */
@Composable
private fun MarcadorFigma(
    numero: String,
    seleccionado: Boolean,
    esCentro: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val fondo = if (seleccionado) Color(0xFF2C2F48) else Obsidian900
    val borde = if (seleccionado) Ambar400 else Slate800

    Box(
        modifier = modifier
            .size(TamanoMarcador)
            .background(fondo, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, borde), RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .semantics {
                contentDescription = "Marcador $numero"
                role = Role.Button
                selected = seleccionado
            },
        contentAlignment = Alignment.Center,
    ) {
        // Ícono central: Estrella o Reloj
        if (esCentro) {
            RelojPixel(seleccionada = seleccionado, tamano = 22.dp)
        } else {
            EstrellaPixel(seleccionada = seleccionado, tamano = 22.dp)
        }

        // Número en la esquina inferior derecha
        Text(
            text = numero,
            style = MaterialTheme.typography.labelSmall.copy(
                fontFamily = PixelifySans,
                fontSize = 8.5.sp,
            ),
            color = if (seleccionado) Ambar400 else Cream200.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 3.dp, bottom = 2.dp),
        )
    }
}
