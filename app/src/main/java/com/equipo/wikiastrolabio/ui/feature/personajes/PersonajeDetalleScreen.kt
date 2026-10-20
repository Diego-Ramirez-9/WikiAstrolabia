package com.equipo.wikiastrolabio.ui.feature.personajes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.GrupoRasgos
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueDialogo
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.FilaInsignias
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.SeccionLectura
import com.equipo.wikiastrolabio.ui.components.TextoLectura
import com.equipo.wikiastrolabio.ui.feature.personajes.components.colorEstado
import com.equipo.wikiastrolabio.ui.feature.personajes.components.textoEstado
import com.equipo.wikiastrolabio.ui.feature.personajes.components.textoRol
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun PersonajeDetalleRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: PersonajeDetalleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PersonajeDetalleScreen(
        uiState = uiState,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/** Ficha completa: retrato, rol y estado, biografía por secciones, rasgos y diálogos. */
@Composable
fun PersonajeDetalleScreen(
    uiState: UiState<Personaje>,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        modifier = modifier,
        encabezado = { personaje ->
            EncabezadoPantalla(
                titulo = personaje?.nombre.orEmpty(),
                antetitulo = stringResource(R.string.personajes_titulo),
                subtitulo = personaje?.titulo,
                onAtras = onAtras,
            )
        },
    ) { personaje ->
        item(key = "retrato") {
            MarcoIlustracion(
                url = personaje.imagenUrl,
                descripcion = personaje.nombre,
                semilla = personaje.slug,
                relacionAspecto = 4f / 3f,
            )
        }
        item(key = "insignias") {
            FilaInsignias {
                textoRol(personaje.rol)?.let { Insignia(it) }
                textoEstado(personaje.estado)?.let { Insignia(it, color = colorEstado(personaje.estado)) }
            }
        }
        item(key = "resumen") { personaje.resumen?.let { TextoLectura(it, capitular = true) } }

        val biografia = listOf(
            R.string.personaje_origen to personaje.origen,
            R.string.personaje_motivacion to personaje.motivacion,
            R.string.personaje_personalidad to personaje.personalidad,
            R.string.personaje_arco to personaje.arco,
        ).filter { !it.second.isNullOrBlank() }
        biografia.forEachIndexed { indice, (titulo, texto) ->
            item(key = "bio-$titulo") { SeccionLectura(stringResource(titulo), texto, numero = indice + 1) }
        }

        if (personaje.rasgos.isNotEmpty()) {
            item(key = "rasgos-titulo") { EtiquetaSeccion(stringResource(R.string.personaje_rasgos)) }
            items(personaje.rasgos, key = { "rasgos-${it.categoria}" }) { grupo -> TarjetaRasgos(grupo) }
        }

        if (personaje.dialogos.isNotEmpty()) {
            item(key = "dialogos-titulo") {
                EtiquetaSeccion(stringResource(R.string.dialogos_titulo), detalle = personaje.dialogos.size.toString().padStart(2, '0'))
            }
            items(personaje.dialogos, key = { "dialogo-${it.slug}" }) { dialogo ->
                BloqueDialogo(dialogo, onSalaClick = onSalaClick)
            }
        }
    }
}

@Composable
private fun TarjetaRasgos(grupo: GrupoRasgos) {
    PanelPixel(separacion = Espaciado.m) {
        Text(
            text = textoCategoriaRasgo(grupo.categoria).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = LocalAcento.current,
        )
        grupo.rasgos.forEachIndexed { indice, rasgo ->
            if (indice > 0) Divisor()
            Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                Text(rasgo.nombre, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
                Text(rasgo.descripcion, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/** Las categorías llegan como texto de la API; las conocidas se traducen y las nuevas se muestran tal cual. */
@Composable
private fun textoCategoriaRasgo(categoria: String): String = when (categoria) {
    "apariencia" -> stringResource(R.string.rasgos_apariencia)
    "expresion" -> stringResource(R.string.rasgos_expresion)
    "herramienta" -> stringResource(R.string.rasgos_herramienta)
    "comunicacion" -> stringResource(R.string.rasgos_comunicacion)
    "presencia" -> stringResource(R.string.rasgos_presencia)
    else -> categoria.replaceFirstChar { it.uppercase() }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1600)
@Composable
private fun PersonajeDetalleScreenPreview() {
    WikiAstrolabioTheme {
        PersonajeDetalleScreen(
            uiState = UiState.Exito(DatosPreview.personaje),
            onAtras = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
