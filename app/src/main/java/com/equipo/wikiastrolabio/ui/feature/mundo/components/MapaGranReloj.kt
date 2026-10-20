package com.equipo.wikiastrolabio.ui.feature.mundo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.common.colorDeTipo
import com.equipo.wikiastrolabio.ui.components.ImagenRemota
import com.equipo.wikiastrolabio.ui.components.pixel.CampoEstelar
import com.equipo.wikiastrolabio.ui.components.pixel.EstrellaPixel
import com.equipo.wikiastrolabio.ui.components.pixel.RelojPixel
import com.equipo.wikiastrolabio.ui.components.pixel.esquinasTecnicas
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Noche700
import com.equipo.wikiastrolabio.ui.theme.Noche800
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

private val TamanoMarcador = 48.dp

/**
 * Mapa del Gran Reloj. Cada [PuntoMapa] trae x/y de 0 a 1 relativos al mapa, así que el
 * marcador se coloca en (x · ancho, y · alto) en cualquier pantalla. Mientras no exista la
 * imagen del mapa en la API, se dibuja el plano técnico del Reloj (diseño del equipo de UI/UX)
 * con las líneas del mecanismo que unen el Eje con cada lugar.
 */
@Composable
fun MapaGranReloj(
    mapa: Mapa,
    slugSeleccionado: String?,
    onPuntoClick: (PuntoMapa) -> Unit,
    modifier: Modifier = Modifier,
) {
    val forma = MaterialTheme.shapes.large
    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(forma)
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, forma)
            .esquinasTecnicas(Ambar400.copy(alpha = 0.5f)),
    ) {
        ImagenRemota(url = mapa.imagenUrl, descripcion = mapa.titulo, modifier = Modifier.fillMaxSize()) {
            CampoEstelar(semilla = "gran-reloj", acento = Ambar400, modifier = Modifier.fillMaxSize().alpha(0.7f))
            PlanoDelReloj(mapa.puntos, Modifier.fillMaxSize())
        }
        mapa.puntos.forEachIndexed { indice, punto ->
            Marcador(
                numero = indice + 1,
                punto = punto,
                seleccionado = punto.slug == slugSeleccionado,
                onClick = { onPuntoClick(punto) },
                modifier = Modifier.offset(
                    x = maxWidth * punto.x - TamanoMarcador / 2,
                    y = maxHeight * punto.y - TamanoMarcador / 2,
                ),
            )
        }
    }
}

/** Plano técnico: marco escalonado, anillos y ejes punteados, y el mecanismo hacia cada lugar. */
@Composable
private fun PlanoDelReloj(puntos: List<PuntoMapa>, modifier: Modifier) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val cx = w * 0.5f
        val cy = h * 0.5f
        val punteado = PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f)
        val fino = Stroke(width = 1.2f)

        drawLine(Noche700, Offset(w * 0.12f, cy), Offset(w * 0.88f, cy), strokeWidth = 1f, pathEffect = punteado)
        drawLine(Noche700, Offset(cx, h * 0.08f), Offset(cx, h * 0.92f), strokeWidth = 1f, pathEffect = punteado)
        drawCircle(Noche700, radius = w * 0.34f, center = Offset(cx, cy), style = Stroke(1f, pathEffect = punteado))
        drawCircle(Noche800, radius = w * 0.18f, center = Offset(cx, cy), style = fino)

        // Marco escalonado en forma de astrolabio.
        val marco = Path().apply {
            moveTo(cx - w * 0.14f, h * 0.04f)
            lineTo(cx + w * 0.14f, h * 0.04f); lineTo(cx + w * 0.14f, h * 0.09f)
            lineTo(cx + w * 0.27f, h * 0.09f); lineTo(cx + w * 0.27f, h * 0.16f)
            lineTo(cx + w * 0.38f, h * 0.16f); lineTo(cx + w * 0.38f, cy - h * 0.15f)
            lineTo(cx + w * 0.46f, cy - h * 0.15f); lineTo(cx + w * 0.46f, cy + h * 0.15f)
            lineTo(cx + w * 0.38f, cy + h * 0.15f); lineTo(cx + w * 0.38f, cy + h * 0.34f)
            lineTo(cx + w * 0.27f, cy + h * 0.34f); lineTo(cx + w * 0.27f, h * 0.91f)
            lineTo(cx + w * 0.14f, h * 0.91f); lineTo(cx + w * 0.14f, h * 0.96f)
            lineTo(cx - w * 0.14f, h * 0.96f); lineTo(cx - w * 0.14f, h * 0.91f)
            lineTo(cx - w * 0.27f, h * 0.91f); lineTo(cx - w * 0.27f, cy + h * 0.34f)
            lineTo(cx - w * 0.38f, cy + h * 0.34f); lineTo(cx - w * 0.38f, cy + h * 0.15f)
            lineTo(cx - w * 0.46f, cy + h * 0.15f); lineTo(cx - w * 0.46f, cy - h * 0.15f)
            lineTo(cx - w * 0.38f, cy - h * 0.15f); lineTo(cx - w * 0.38f, h * 0.16f)
            lineTo(cx - w * 0.27f, h * 0.16f); lineTo(cx - w * 0.27f, h * 0.09f)
            lineTo(cx - w * 0.14f, h * 0.09f)
            close()
        }
        drawPath(marco, color = Noche800, style = Stroke(width = 1.5f))

        // El mecanismo: del centro del Reloj a cada lugar.
        puntos.forEach { punto ->
            drawLine(
                color = colorDeTipo(punto.tipo).copy(alpha = 0.35f),
                start = Offset(cx, cy),
                end = Offset(w * punto.x, h * punto.y),
                strokeWidth = 1.5f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(2f, 6f), 0f),
            )
        }
        drawCircle(Ambar400.copy(alpha = 0.8f), radius = 4f, center = Offset(cx, cy))
    }
}

@Composable
private fun Marcador(
    numero: Int,
    punto: PuntoMapa,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = colorDeTipo(punto.tipo)
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.small,
        color = if (seleccionado) color.copy(alpha = 0.22f) else MaterialTheme.colorScheme.surfaceContainerLow,
        border = BorderStroke(if (seleccionado) 2.dp else 1.dp, if (seleccionado) color else color.copy(alpha = 0.45f)),
        modifier = modifier
            .size(TamanoMarcador)
            .semantics {
                contentDescription = punto.nombre
                role = Role.Button
                selected = seleccionado
            },
    ) {
        Box(contentAlignment = Alignment.Center) {
            if (punto.tipo == TipoZona.CIERRE) {
                RelojPixel(color = color, tamano = 24.dp)
            } else {
                EstrellaPixel(seleccionada = seleccionado, color = color, tamano = 24.dp)
            }
            Text(
                text = numero.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelSmall,
                color = if (seleccionado) color else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 3.dp, bottom = 1.dp),
            )
        }
    }
}

@Preview
@Composable
private fun MapaGranRelojPreview() {
    WikiAstrolabioTheme {
        MapaGranReloj(mapa = DatosPreview.mapa, slugSeleccionado = "geminis", onPuntoClick = {})
    }
}
