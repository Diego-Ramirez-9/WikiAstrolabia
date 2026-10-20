package com.equipo.wikiastrolabio.ui.feature.archivos.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.FilaIndice
import com.equipo.wikiastrolabio.ui.components.MarcoIcono
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.pixel.IconoArchivos
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Colección desplegable de la pantalla Archivos: cabecera con conteo y, abierta, sus registros. */
@Composable
fun AcordeonCategoria(
    categoria: CategoriaArchivo,
    indice: Int,
    expandida: Boolean,
    onAlternar: () -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val acento = LocalAcento.current
    val giro by animateFloatAsState(if (expandida) 90f else 0f, label = "giro")
    val estado = stringResource(if (expandida) R.string.estado_desplegado else R.string.estado_plegado)

    PanelPixel(modifier = modifier, resaltado = expandida, relleno = PaddingValues(0.dp), separacion = 0.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onAlternar, role = Role.Button)
                .semantics { stateDescription = estado }
                .padding(Espaciado.l),
            horizontalArrangement = Arrangement.spacedBy(Espaciado.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            MarcoIcono(url = categoria.iconoUrl, tamano = 48.dp) { IconoArchivos(tamano = 28.dp) }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(Espaciado.xxs)) {
                Text(
                    text = stringResource(R.string.archivos_coleccion, indice.toString().padStart(2, '0')).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = acento,
                )
                Text(categoria.nombre.uppercase(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
                Text(
                    text = pluralStringResource(R.plurals.archivos_registros, categoria.elementos.size, categoria.elementos.size).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                painter = painterResource(R.drawable.ic_flecha_derecha),
                contentDescription = null,
                tint = acento,
                modifier = Modifier
                    .size(18.dp)
                    .rotate(giro),
            )
        }
        AnimatedVisibility(visible = expandida) {
            Column(modifier = Modifier.padding(start = Espaciado.l, end = Espaciado.l, bottom = Espaciado.s)) {
                Divisor()
                categoria.descripcion?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = Espaciado.m),
                    )
                }
                if (categoria.elementos.isEmpty()) {
                    Text(
                        text = stringResource(R.string.archivos_vacio),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = Espaciado.m),
                    )
                }
                categoria.elementos.forEachIndexed { posicion, archivo ->
                    if (posicion > 0) Divisor()
                    FilaIndice(
                        titulo = archivo.titulo,
                        subtitulo = archivo.ubicacion,
                        numero = posicion + 1,
                        onClick = { onArchivoClick(archivo.slug) },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C)
@Composable
private fun AcordeonCategoriaPreview() {
    WikiAstrolabioTheme {
        AcordeonCategoria(
            categoria = DatosPreview.categoriasArchivo.first(),
            indice = 1,
            expandida = true,
            onAlternar = {},
            onArchivoClick = {},
            modifier = Modifier.padding(Espaciado.l),
        )
    }
}
