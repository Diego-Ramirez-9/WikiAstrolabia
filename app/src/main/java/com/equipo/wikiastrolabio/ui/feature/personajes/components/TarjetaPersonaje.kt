package com.equipo.wikiastrolabio.ui.feature.personajes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Tarjeta del catálogo de personajes: ilustración, nombre, rol y resumen. */
@Composable
fun TarjetaPersonaje(
    personaje: PersonajeResumen,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(Espaciado.m),
            verticalArrangement = Arrangement.spacedBy(Espaciado.s),
        ) {
            ImagenPixelArt(
                url = personaje.imagenUrl,
                contentDescription = personaje.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
            )
            Text(personaje.nombre, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
            textoRol(personaje.rol)?.let { Text(it, style = MaterialTheme.typography.labelMedium) }
            personaje.resumen?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
        }
    }
}

@Composable
fun textoRol(rol: RolPersonaje): String? = when (rol) {
    RolPersonaje.PROTAGONISTA -> stringResource(R.string.rol_protagonista)
    RolPersonaje.GUIA -> stringResource(R.string.rol_guia)
    RolPersonaje.SECUNDARIO -> stringResource(R.string.rol_secundario)
    RolPersonaje.COLECTIVO -> stringResource(R.string.rol_colectivo)
    RolPersonaje.DESCONOCIDO -> null
}

@Composable
fun textoEstado(estado: EstadoPersonaje): String? = when (estado) {
    EstadoPersonaje.ACTIVO -> stringResource(R.string.estado_activo)
    EstadoPersonaje.DESACTIVADO -> stringResource(R.string.estado_desactivado)
    EstadoPersonaje.DESAPARECIDO -> stringResource(R.string.estado_desaparecido)
    EstadoPersonaje.DESCONOCIDO -> null
}

@Preview
@Composable
private fun TarjetaPersonajePreview() {
    WikiAstrolabioTheme {
        TarjetaPersonaje(personaje = DatosPreview.personajes.first(), onClick = {})
    }
}
