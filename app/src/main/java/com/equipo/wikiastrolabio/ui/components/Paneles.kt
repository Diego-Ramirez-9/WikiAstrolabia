package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Contenedor base de todas las tarjetas: esquinas cortadas, fondo un tono arriba del fondo
 * de la app y borde fino. Con [resaltado] el borde toma el color de acento (ej. seleccionado).
 * Con [onClick] todo el panel es tocable.
 */
@Composable
fun PanelPixel(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    resaltado: Boolean = false,
    color: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    relleno: PaddingValues = PaddingValues(Espaciado.rellenoPanel),
    separacion: Dp = Espaciado.s,
    contenido: @Composable ColumnScope.() -> Unit,
) {
    val borde = BorderStroke(
        width = 1.dp,
        color = if (resaltado) LocalAcento.current else MaterialTheme.colorScheme.outlineVariant,
    )
    val interior: @Composable () -> Unit = {
        Column(
            modifier = Modifier.padding(relleno),
            verticalArrangement = Arrangement.spacedBy(separacion),
            content = contenido,
        )
    }
    if (onClick != null) {
        Surface(
            onClick = onClick,
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = color,
            border = borde,
            content = interior,
        )
    } else {
        Surface(
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = color,
            border = borde,
            content = interior,
        )
    }
}

/**
 * Rótulo de sección en versalitas con una línea que corre hasta el borde:
 * "02  HABITACIONES ───────". Marcado como encabezado para TalkBack.
 */
@Composable
fun EtiquetaSeccion(
    texto: String,
    modifier: Modifier = Modifier,
    numero: Int? = null,
    detalle: String? = null,
) {
    val acento = LocalAcento.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Espaciado.xl, bottom = Espaciado.xs)
            .semantics(mergeDescendants = true) { heading() },
        horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (numero != null) {
            Text(
                text = numero.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelMedium,
                color = acento,
            )
        }
        Text(
            text = texto.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
        if (detalle != null) {
            Text(
                text = detalle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(MaterialTheme.colorScheme.outlineVariant),
        )
    }
}

/** Línea divisoria fina entre filas de una lista. */
@Composable
fun Divisor(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant),
    )
}
