package com.equipo.wikiastrolabio.ui.feature.zonas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Pestañas desplazables con todas las zonas del Reloj, para saltar de una a otra sin regresar
 * al mapa. La seleccionada se rellena con el acento de la zona actual.
 */
@Composable
fun SelectorZonas(
    zonas: List<ZonaResumen>,
    slugSeleccionado: String,
    onZonaClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val acento = LocalAcento.current
    val estado = rememberLazyListState()
    LaunchedEffect(slugSeleccionado, zonas) {
        val indice = zonas.indexOfFirst { it.slug == slugSeleccionado }
        if (indice >= 0) estado.animateScrollToItem(indice)
    }
    LazyRow(
        state = estado,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
    ) {
        items(zonas, key = { it.slug }) { zona ->
            val activa = zona.slug == slugSeleccionado
            Surface(
                onClick = { onZonaClick(zona.slug) },
                shape = MaterialTheme.shapes.small,
                color = if (activa) acento else MaterialTheme.colorScheme.surfaceContainerLow,
                border = BorderStroke(1.dp, if (activa) acento else MaterialTheme.colorScheme.outline),
                modifier = Modifier.semantics {
                    role = Role.Tab
                    selected = activa
                },
            ) {
                Text(
                    text = zona.nombre.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    color = if (activa) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = Espaciado.l, vertical = Espaciado.m),
                )
            }
        }
    }
}
