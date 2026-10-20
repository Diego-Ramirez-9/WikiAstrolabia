package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Ficha técnica: pares clave → valor dentro de un panel (paleta, iluminación, parámetros…).
 * Omite las filas sin valor y no dibuja nada si ninguna tiene.
 */
@Composable
fun FichaDatos(
    filas: List<Pair<String, String?>>,
    modifier: Modifier = Modifier,
) {
    val visibles = filas.filter { !it.second.isNullOrBlank() }
    if (visibles.isEmpty()) return
    val acento = LocalAcento.current
    PanelPixel(modifier = modifier, separacion = Espaciado.m) {
        visibles.forEachIndexed { indice, (clave, valor) ->
            if (indice > 0) Divisor()
            Column(
                modifier = Modifier.padding(top = if (indice > 0) Espaciado.xs else Espaciado.xxs),
                verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
            ) {
                Text(clave.uppercase(), style = MaterialTheme.typography.labelSmall, color = acento)
                Text(valor.orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
