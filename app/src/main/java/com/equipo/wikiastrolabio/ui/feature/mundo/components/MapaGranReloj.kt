package com.equipo.wikiastrolabio.ui.feature.mundo.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Tamaño táctil mínimo recomendado por accesibilidad. */
private val TamanoMarcador = 48.dp

/**
 * Mapa del Gran Reloj: la imagen de la API con un marcador por zona.
 *
 * Cada [PuntoMapa] trae x/y de 0 a 1 relativos a la imagen, así que el marcador se coloca en
 * (x * ancho, y * alto) y queda en su lugar en cualquier tamaño de pantalla. Si cambia la
 * imagen del mapa en Supabase, solo hay que ajustar las coordenadas en la base de datos.
 *
 * [relacionAspecto] debe coincidir con la proporción de la imagen final (PROVISIONAL: 1:1).
 */
@Composable
fun MapaGranReloj(
    mapa: Mapa,
    slugSeleccionado: String?,
    onPuntoClick: (PuntoMapa) -> Unit,
    modifier: Modifier = Modifier,
    relacionAspecto: Float = 1f,
) {
    BoxWithConstraints(modifier = modifier.aspectRatio(relacionAspecto)) {
        ImagenPixelArt(
            url = mapa.imagenUrl,
            contentDescription = mapa.titulo,
            modifier = Modifier.fillMaxSize(),
        )
        mapa.puntos.forEachIndexed { indice, punto ->
            Marcador(
                numero = indice + 1,
                punto = punto,
                seleccionado = punto.slug == slugSeleccionado,
                onClick = { onPuntoClick(punto) },
                modifier = Modifier.offset(
                    x = maxWidth * punto.x - TamanoMarcador / 2,
                    y = maxHeight * punto.y - TamanoMarcador / 2,
                ),
            )
        }
    }
}

/** PROVISIONAL: círculo numerado. UI/UX lo reemplaza por el sprite de estrella del Figma. */
@Composable
private fun Marcador(
    numero: Int,
    punto: PuntoMapa,
    seleccionado: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colores = MaterialTheme.colorScheme
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = if (seleccionado) colores.primary else colores.surfaceContainerHigh,
        contentColor = if (seleccionado) colores.onPrimary else colores.onSurface,
        border = BorderStroke(1.dp, colores.primary),
        modifier = modifier
            .size(TamanoMarcador)
            .semantics {
                contentDescription = punto.nombre
                role = Role.Button
                selected = seleccionado
            },
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(numero.toString().padStart(2, '0'), style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Preview
@Composable
private fun MapaGranRelojPreview() {
    WikiAstrolabioTheme {
        MapaGranReloj(mapa = DatosPreview.mapa, slugSeleccionado = "geminis", onPuntoClick = {})
    }
}
