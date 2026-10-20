package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Insignia de estado o clasificación ("OBSERVATORIO", "PROTAGONISTA", "TUTORIAL"), en
 * versalitas y con el color de acento. Para etiquetas descriptivas usa [Etiquetas].
 */
@Composable
fun Insignia(
    texto: String,
    modifier: Modifier = Modifier,
    color: Color = LocalAcento.current,
    /** false para valores de la API con unidades ("3–5 s"): en pixel, "S" mayúscula parece un 5. */
    mayusculas: Boolean = true,
    icono: (@Composable () -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color.copy(alpha = 0.45f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Espaciado.s, vertical = Espaciado.xs),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            icono?.invoke()
            Text(if (mayusculas) texto.uppercase() else texto, style = MaterialTheme.typography.labelSmall, color = color)
        }
    }
}

/** Fila de insignias y enlaces que salta de línea si no caben (centrados en vertical). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilaInsignias(modifier: Modifier = Modifier, contenido: @Composable () -> Unit) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
        verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
        itemVerticalAlignment = Alignment.CenterVertically,
    ) { contenido() }
}

/**
 * Enlace a otro elemento del códex ("Casa de Géminis", "Habla Efeméride") con forma de chip
 * y flecha. Su área tocable mide 48 dp aunque el chip se vea más pequeño.
 */
@Composable
fun ChipEnlace(texto: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val acento = LocalAcento.current
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = acento.copy(alpha = 0.06f),
        border = BorderStroke(1.dp, acento.copy(alpha = 0.6f)),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = Espaciado.s, vertical = Espaciado.xs + Espaciado.xxs),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.xs),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(texto.uppercase(), style = MaterialTheme.typography.labelSmall, color = acento)
            Icon(
                painter = painterResource(R.drawable.ic_flecha_derecha),
                contentDescription = null,
                tint = acento,
                modifier = Modifier.size(12.dp),
            )
        }
    }
}

/** Etiquetas descriptivas de solo lectura ("Dualidad y reflejos", "Muros gemelos"). */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Etiquetas(etiquetas: List<String>, modifier: Modifier = Modifier) {
    if (etiquetas.isEmpty()) return
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
        verticalArrangement = Arrangement.spacedBy(Espaciado.s),
    ) {
        etiquetas.forEach { etiqueta ->
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = Espaciado.m, vertical = Espaciado.s),
                )
            }
        }
    }
}
