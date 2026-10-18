package com.equipo.wikiastrolabio.ui.feature.zonas.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.ui.components.TarjetaNavegable

/** Habitación dentro de la lista de una zona: número de orden, nombre y si es observatorio o zona segura. */
@Composable
fun FilaSala(sala: SalaResumen, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val distintivos = listOfNotNull(
        stringResource(R.string.sala_observatorio).takeIf { sala.esObservatorio },
        stringResource(R.string.sala_zona_segura).takeIf { sala.esZonaSegura },
    )
    TarjetaNavegable(
        titulo = sala.nombre,
        subtitulo = distintivos.joinToString(" · ").ifEmpty { null },
        onClick = onClick,
        modifier = modifier,
        inicio = {
            Text(
                text = sala.orden.toString().padStart(2, '0'),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
    )
}
