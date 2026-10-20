package com.equipo.wikiastrolabio.ui.feature.mecanicas.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Habilidad
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.FilaInsignias
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.MarcoIcono
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.pixel.IconoEngranaje
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/**
 * Habilidad jugable del manual de campo: ícono, número, nombre, chip de la API, descripción,
 * parámetros de ajuste y, si existen, su origen en el mundo y la señal que ve el jugador.
 */
@Composable
fun TarjetaHabilidad(habilidad: Habilidad, modifier: Modifier = Modifier) {
    val acento = LocalAcento.current
    PanelPixel(modifier = modifier, separacion = Espaciado.m) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m), verticalAlignment = Alignment.CenterVertically) {
            MarcoIcono(url = habilidad.iconoUrl) { IconoEngranaje(tamano = 28.dp, color = acento) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espaciado.xxs)) {
                Text(
                    text = stringResource(R.string.habilidad_numero, habilidad.numero.toString().padStart(2, '0')).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = acento,
                )
                Text(habilidad.nombreCorto.uppercase(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                if (!habilidad.nombre.equals(habilidad.nombreCorto, ignoreCase = true)) {
                    Text(habilidad.nombre, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.tertiary)
                }
            }
        }
        habilidad.etiqueta?.let { Insignia(it, mayusculas = false) }
        Text(habilidad.descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)

        if (habilidad.parametros.isNotEmpty()) {
            FilaInsignias {
                habilidad.parametros.forEach { (clave, valor) -> Parametro(etiquetaParametro(clave), valor) }
            }
        }
        if (!habilidad.origenMundo.isNullOrBlank() || !habilidad.senalJugador.isNullOrBlank()) {
            Divisor()
            Nota(stringResource(R.string.habilidad_origen), habilidad.origenMundo)
            Nota(stringResource(R.string.habilidad_senal), habilidad.senalJugador)
        }
    }
}

@Composable
private fun Parametro(etiqueta: String, valor: String) {
    Surface(
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = Espaciado.m, vertical = Espaciado.s),
            verticalArrangement = Arrangement.spacedBy(Espaciado.xxs),
        ) {
            Text(etiqueta.uppercase(), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(valor, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
        }
    }
}

@Composable
private fun Nota(titulo: String, texto: String?) {
    if (texto.isNullOrBlank()) return
    Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xxs)) {
        Text(titulo.uppercase(), style = MaterialTheme.typography.labelSmall, color = LocalAcento.current)
        Text(texto, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * Convierte la clave de un parámetro de la API en una etiqueta legible:
 * "duracion_s_max" → "Duración máx. (s)", "max_activos" → "Máx. activos".
 * Las palabras que no están en el diccionario se muestran tal cual.
 */
fun etiquetaParametro(clave: String): String {
    val palabras = mutableListOf<String>()
    val unidades = mutableListOf<String>()
    clave.split('_').filter { it.isNotBlank() }.forEach { parte ->
        when (val unidad = UNIDADES[parte]) {
            null -> palabras += PALABRAS[parte] ?: parte
            else -> unidades += unidad
        }
    }
    return (palabras + unidades).joinToString(" ").replaceFirstChar { it.uppercase() }
}

private val UNIDADES = mapOf("s" to "(s)", "unidades" to "(u)")

private val PALABRAS = mapOf(
    "alcance" to "alcance",
    "altura" to "altura",
    "duracion" to "duración",
    "max" to "máx.",
    "min" to "mín.",
    "activos" to "activos",
    "usos" to "usos",
    "recarga" to "recarga",
    "superficies" to "superficies",
)

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun TarjetaHabilidadPreview() {
    WikiAstrolabioTheme {
        TarjetaHabilidad(DatosPreview.mecanicas.habilidades[1], modifier = Modifier.padding(Espaciado.l))
    }
}
