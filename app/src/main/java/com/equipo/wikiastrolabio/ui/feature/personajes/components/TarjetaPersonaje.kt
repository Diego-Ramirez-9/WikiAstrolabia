package com.equipo.wikiastrolabio.ui.feature.personajes.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Ficha del catálogo: retrato a la izquierda y nombre, rol y resumen a la derecha. */
@Composable
fun TarjetaPersonaje(
    personaje: PersonajeResumen,
    numero: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PanelPixel(modifier = modifier, onClick = onClick) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.l)) {
            MarcoIlustracion(
                url = personaje.imagenUrl,
                descripcion = personaje.nombre,
                semilla = personaje.slug,
                relacionAspecto = 0.8f,
                mostrarLeyenda = false,
                modifier = Modifier.width(96.dp),
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Espaciado.s),
            ) {
                Text(
                    text = stringResource(R.string.personajes_numero, numero.toString().padStart(2, '0')).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = LocalAcento.current,
                )
                Text(personaje.nombre.uppercase(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                textoRol(personaje.rol)?.let { Insignia(it) }
                personaje.resumen?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 4,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = Espaciado.xxs),
                    )
                }
            }
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

/** Activo con el acento; desactivado y desaparecido en tonos apagados o de alerta. */
@Composable
fun colorEstado(estado: EstadoPersonaje): Color = when (estado) {
    EstadoPersonaje.ACTIVO -> LocalAcento.current
    EstadoPersonaje.DESACTIVADO -> MaterialTheme.colorScheme.onSurfaceVariant
    EstadoPersonaje.DESAPARECIDO -> MaterialTheme.colorScheme.error
    EstadoPersonaje.DESCONOCIDO -> MaterialTheme.colorScheme.onSurfaceVariant
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun TarjetaPersonajePreview() {
    WikiAstrolabioTheme {
        TarjetaPersonaje(
            personaje = DatosPreview.personajes.first(),
            numero = 1,
            onClick = {},
            modifier = Modifier.padding(Espaciado.l),
        )
    }
}
