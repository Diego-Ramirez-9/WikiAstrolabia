package com.equipo.wikiastrolabio.ui.feature.personajes

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BloqueDialogo
import com.equipo.wikiastrolabio.ui.components.BloqueTexto
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.feature.personajes.components.textoEstado
import com.equipo.wikiastrolabio.ui.feature.personajes.components.textoRol
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun PersonajeDetalleRoute(
    onAtras: () -> Unit,
    viewModel: PersonajeDetalleViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PersonajeDetalleScreen(
        uiState = uiState,
        onAtras = onAtras,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

@Composable
fun PersonajeDetalleScreen(
    uiState: UiState<Personaje>,
    onAtras: () -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        titulo = (uiState as? UiState.Exito)?.datos?.nombre.orEmpty(),
        uiState = uiState,
        onReintentar = onReintentar,
        onRecargar = onRecargar,
        onAtras = onAtras,
        modifier = modifier,
    ) { personaje ->
        item {
            ImagenPixelArt(
                url = personaje.imagenUrl,
                contentDescription = personaje.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            )
        }
        personaje.titulo?.let { titulo ->
            item { Text(titulo, style = MaterialTheme.typography.titleMedium) }
        }
        item {
            val etiquetas = listOfNotNull(textoRol(personaje.rol), textoEstado(personaje.estado))
            Text(etiquetas.joinToString(" · "), style = MaterialTheme.typography.labelMedium)
        }
        item { BloqueTexto(stringResource(R.string.personaje_origen), personaje.origen) }
        item { BloqueTexto(stringResource(R.string.personaje_motivacion), personaje.motivacion) }
        item { BloqueTexto(stringResource(R.string.personaje_personalidad), personaje.personalidad) }
        item { BloqueTexto(stringResource(R.string.personaje_arco), personaje.arco) }

        personaje.rasgos.forEach { grupo ->
            item(key = "rasgos-${grupo.categoria}") { EncabezadoSeccion(textoCategoriaRasgo(grupo.categoria)) }
            items(grupo.rasgos) { rasgo ->
                Text("${rasgo.nombre}: ${rasgo.descripcion}", style = MaterialTheme.typography.bodyMedium)
            }
        }

        if (personaje.dialogos.isNotEmpty()) {
            item { EncabezadoSeccion(stringResource(R.string.personaje_dialogos)) }
            items(personaje.dialogos, key = { it.slug }) { dialogo -> BloqueDialogo(dialogo) }
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

@Preview(showBackground = true)
@Composable
private fun PersonajeDetalleScreenPreview() {
    WikiAstrolabioTheme {
        PersonajeDetalleScreen(
            uiState = UiState.Exito(DatosPreview.personaje),
            onAtras = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
