package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento

/**
 * Encabezado de pantalla: botón de regresar, antetítulo en el color de acento, título en
 * versalitas pixel y subtítulo de lectura. Se desplaza junto con el contenido.
 *
 * [principal] = pantallas raíz (título más grande); los detalles usan el tamaño menor para
 * que nombres largos ("El Observatorio de las Dos Lentes") quepan en dos líneas.
 */
@Composable
fun EncabezadoPantalla(
    titulo: String,
    modifier: Modifier = Modifier,
    antetitulo: String? = null,
    subtitulo: String? = null,
    onAtras: (() -> Unit)? = null,
    principal: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = Espaciado.s, bottom = Espaciado.l),
        verticalArrangement = Arrangement.spacedBy(Espaciado.s),
    ) {
        if (onAtras != null) {
            BotonAtras(onClick = onAtras, modifier = Modifier.padding(bottom = Espaciado.s))
        }
        if (antetitulo != null) {
            Text(
                text = antetitulo.uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = LocalAcento.current,
            )
        }
        Text(
            text = titulo.uppercase(),
            style = if (principal) MaterialTheme.typography.displaySmall else MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.semantics { heading() },
        )
        if (!subtitulo.isNullOrBlank()) {
            Text(
                text = subtitulo,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
