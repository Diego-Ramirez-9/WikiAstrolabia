package com.equipo.wikiastrolabio.ui.feature.mecanicas.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Habilidad
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.Etiquetas
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Tarjeta de una habilidad jugable: ícono, número, nombre corto, descripción y chip de parámetros. */
@Composable
fun TarjetaHabilidad(habilidad: Habilidad, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Row(
            modifier = Modifier.padding(Espaciado.m),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
        ) {
            ImagenPixelArt(
                url = habilidad.iconoUrl,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )
            Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(
                    text = "${habilidad.nombreCorto}  ${habilidad.numero.toString().padStart(2, '0')}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(habilidad.descripcion, style = MaterialTheme.typography.bodyMedium)
                habilidad.etiqueta?.let { Etiquetas(listOf(it)) }
                BloqueTexto(stringResource(R.string.habilidad_origen), habilidad.origenMundo)
                BloqueTexto(stringResource(R.string.habilidad_senal), habilidad.senalJugador)
            }
        }
    }
}

@Preview
@Composable
private fun TarjetaHabilidadPreview() {
    WikiAstrolabioTheme {
        TarjetaHabilidad(DatosPreview.mecanicas.habilidades.first())
    }
}
