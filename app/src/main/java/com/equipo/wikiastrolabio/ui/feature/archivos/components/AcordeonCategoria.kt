package com.equipo.wikiastrolabio.ui.feature.archivos.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.ui.components.ImagenPixelArt
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Colección desplegable de la pantalla Archivos (Poemas, Notas de XI…). */
@Composable
fun AcordeonCategoria(
    categoria: CategoriaArchivo,
    expandida: Boolean,
    onAlternar: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val accion = stringResource(if (expandida) R.string.accion_contraer else R.string.accion_expandir, categoria.nombre)
    Card(
        onClick = onAlternar,
        modifier = modifier
            .fillMaxWidth()
            .semantics { stateDescription = accion },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(Espaciado.m),
            verticalArrangement = Arrangement.spacedBy(Espaciado.s),
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ImagenPixelArt(url = categoria.iconoUrl, contentDescription = null, modifier = Modifier.size(40.dp))
                Text(
                    text = categoria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                )
                Text(if (expandida) "−" else "+", style = MaterialTheme.typography.titleLarge)
            }
            AnimatedVisibility(visible = expandida) {
                Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                    categoria.descripcion?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                    if (categoria.elementos.isEmpty()) {
                        Text(stringResource(R.string.archivos_vacio), style = MaterialTheme.typography.bodySmall)
                    }
                    categoria.elementos.forEach { archivo ->
                        TextButton(onClick = { onArchivoClick(archivo.slug) }) {
                            Text(
                                text = listOfNotNull(archivo.titulo, archivo.ubicacion).joinToString(" · "),
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AcordeonCategoriaPreview() {
    WikiAstrolabioTheme {
        AcordeonCategoria(
            categoria = DatosPreview.categoriasArchivo.first(),
            expandida = true,
            onAlternar = {},
            onArchivoClick = {},
        )
    }
}
