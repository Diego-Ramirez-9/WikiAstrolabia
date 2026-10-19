package com.equipo.wikiastrolabio.ui.feature.personajes.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.FuentePixelFallback
import com.equipo.wikiastrolabio.ui.theme.FuentePixelTitulosFallback
import com.equipo.wikiastrolabio.ui.theme.Obsidian900
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate800
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Tarjeta del catálogo de personajes rediseñada según el prototipo de Figma:
 * - Etiqueta superior "PERSONAJE 01", "PERSONAJE 02", etc.
 * - Recuadro técnico para la ilustración con esquinas de astrolabio.
 * - Nombre del personaje en color terracota ("ALMANAQUE XII", "EPHEMERIS").
 * - Resumen conciso en tipografía retro pixel/monospace.
 * - Separador en Slate-800.
 * - Pie de acción "VER MÁS" con flecha ámbar pixel art.
 */
@Composable
fun TarjetaPersonaje(
    personaje: PersonajeResumen,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    indice: Int = 1,
) {
    val nombreMostrado = if (personaje.slug == "efemeride") {
        "EPHEMERIS"
    } else {
        personaje.nombre.uppercase()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Obsidian900, RoundedCornerShape(2.dp))
            .border(BorderStroke(1.dp, Slate800), RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .padding(16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            // 1. Etiqueta superior "PERSONAJE 01"
            Text(
                text = "PERSONAJE ${indice.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontFamily = FuentePixelFallback,
                    fontWeight = FontWeight.Bold,
                    fontSize = 9.5.sp,
                    letterSpacing = 1.2.sp,
                ),
                color = Sand300,
            )

            // 2. Contenedor de la ilustración
            ContenedorImagenPersonaje(
                nombrePersonaje = personaje.nombre,
                imagenUrl = personaje.imagenUrl,
            )

            // 3. Nombre del personaje en TerraCotta500
            Text(
                text = nombreMostrado,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = FuentePixelTitulosFallback,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp,
                    letterSpacing = 1.2.sp,
                ),
                color = TerraCotta500,
                modifier = Modifier.padding(top = 2.dp),
            )

            // 4. Resumen / descripción
            personaje.resumen?.let { resumen ->
                Text(
                    text = resumen,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = FuentePixelFallback,
                        fontSize = 12.5.sp,
                        lineHeight = 18.sp,
                        letterSpacing = 0.3.sp,
                    ),
                    color = Cream200,
                )
            }

            // 5. Divisor horizontal
            HorizontalDivider(
                thickness = 1.dp,
                color = Slate800,
                modifier = Modifier.padding(top = 4.dp),
            )

            // 6. Pie de tarjeta "VER MÁS ➔"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "VER MÁS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = FuentePixelFallback,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                    ),
                    color = Ambar400,
                )

                Icon(
                    painter = painterResource(R.drawable.ic_flecha_derecha_pixel),
                    contentDescription = null,
                    tint = Ambar400,
                    modifier = Modifier.size(14.dp),
                )
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

@Preview(showBackground = true)
@Composable
private fun TarjetaPersonajePreview() {
    WikiAstrolabioTheme {
        TarjetaPersonaje(
            personaje = DatosPreview.personajes.first(),
            indice = 1,
            onClick = {},
        )
    }
}
