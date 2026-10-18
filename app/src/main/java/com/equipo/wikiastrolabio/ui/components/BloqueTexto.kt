package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.ui.theme.Espaciado

/** Título de una sección dentro de una pantalla. Marcado como encabezado para TalkBack. */
@Composable
fun EncabezadoSeccion(texto: String, modifier: Modifier = Modifier) {
    Text(
        text = texto,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
            .padding(top = Espaciado.m, bottom = Espaciado.xs)
            .semantics { heading() },
    )
}

/** Título + párrafo. Si [texto] es null o está vacío no pinta nada (la API puede omitir campos). */
@Composable
fun BloqueTexto(titulo: String, texto: String?, modifier: Modifier = Modifier) {
    if (texto.isNullOrBlank()) return
    Column(modifier = modifier) {
        EncabezadoSeccion(titulo)
        Text(text = texto, style = MaterialTheme.typography.bodyMedium)
    }
}

/** Chips de solo lectura, ej. "Dualidad y reflejos", "Muros gemelos". */
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
                color = MaterialTheme.colorScheme.surfaceContainer,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            ) {
                Text(
                    text = etiqueta,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = Espaciado.s, vertical = Espaciado.xs),
                )
            }
        }
    }
}
