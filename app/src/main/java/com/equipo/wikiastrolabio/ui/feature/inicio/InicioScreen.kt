package com.equipo.wikiastrolabio.ui.feature.inicio

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.components.EncabezadoSeccion
import com.equipo.wikiastrolabio.ui.components.TarjetaNavegable
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Secciones del códex que se muestran en Inicio, en este orden. */
enum class SeccionCodex(@param:StringRes val titulo: Int, @param:StringRes val descripcion: Int) {
    HISTORIA(R.string.codex_historia, R.string.codex_historia_desc),
    MUNDO(R.string.codex_mundo, R.string.codex_mundo_desc),
    PERSONAJES(R.string.codex_personajes, R.string.codex_personajes_desc),
    MECANICAS(R.string.codex_mecanicas, R.string.codex_mecanicas_desc),
    ARCHIVOS(R.string.codex_archivos, R.string.codex_archivos_desc),
}

/** Pantalla Inicio: no consume la API, solo da acceso a cada sección del códex. */
@Composable
fun InicioScreen(
    onSeccionClick: (SeccionCodex) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(Espaciado.margenPantalla),
        verticalArrangement = Arrangement.spacedBy(Espaciado.s),
    ) {
        item {
            Column(
                modifier = Modifier.padding(bottom = Espaciado.m),
                verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
            ) {
                Text(
                    text = stringResource(R.string.inicio_titulo),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.semantics { heading() },
                )
                Text(stringResource(R.string.inicio_registro), style = MaterialTheme.typography.labelSmall)
                Text(stringResource(R.string.inicio_lema), style = MaterialTheme.typography.titleLarge)
                Text(stringResource(R.string.inicio_subtitulo), style = MaterialTheme.typography.bodyMedium)
            }
        }
        item { EncabezadoSeccion(stringResource(R.string.inicio_explora)) }
        items(SeccionCodex.entries) { seccion ->
            TarjetaNavegable(
                titulo = stringResource(seccion.titulo),
                subtitulo = stringResource(seccion.descripcion),
                onClick = { onSeccionClick(seccion) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun InicioScreenPreview() {
    WikiAstrolabioTheme {
        InicioScreen(onSeccionClick = {})
    }
}
