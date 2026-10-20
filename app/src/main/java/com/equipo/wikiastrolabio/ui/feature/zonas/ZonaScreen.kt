package com.equipo.wikiastrolabio.ui.feature.zonas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.common.acentoDeZona
import com.equipo.wikiastrolabio.ui.components.CitaDestacada
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.Etiquetas
import com.equipo.wikiastrolabio.ui.components.FichaDatos
import com.equipo.wikiastrolabio.ui.components.FilaIndice
import com.equipo.wikiastrolabio.ui.components.FilaInsignias
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.SeccionLectura
import com.equipo.wikiastrolabio.ui.components.TextoLectura
import com.equipo.wikiastrolabio.ui.components.pixel.IconoEngranaje
import com.equipo.wikiastrolabio.ui.components.pixel.IconoEstrellaGuia
import com.equipo.wikiastrolabio.ui.components.pixel.IconoObservatorio
import com.equipo.wikiastrolabio.ui.components.pixel.IconoZonaSegura
import com.equipo.wikiastrolabio.ui.feature.mundo.textoTipo
import com.equipo.wikiastrolabio.ui.feature.zonas.components.SelectorZonas
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.ConAcento
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun ZonaRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    viewModel: ZonaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val pestanas by viewModel.pestanas.collectAsStateWithLifecycle()
    val slugSeleccionado by viewModel.slugSeleccionado.collectAsStateWithLifecycle()
    ZonaScreen(
        uiState = uiState,
        pestanas = pestanas,
        slugSeleccionado = slugSeleccionado,
        onPestanaClick = viewModel::seleccionarZona,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/**
 * Ficha completa de una zona, teñida con su color (violeta en Géminis, turquesa en Acuario…):
 * identidad, recorrido de salas, ambientación, mecánicas propias y dirección de arte.
 */
@Composable
fun ZonaScreen(
    uiState: UiState<Zona>,
    pestanas: List<ZonaResumen>,
    slugSeleccionado: String,
    onPestanaClick: (slug: String) -> Unit,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val zonaActual = (uiState as? UiState.Exito)?.datos
    val acento = zonaActual?.let { acentoDeZona(it.tipo, it.tinte) } ?: Ambar400

    ConAcento(acento) {
        PantallaContenido(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
            modifier = modifier,
            encabezado = { zona ->
                Column {
                    EncabezadoPantalla(
                        titulo = zona?.nombre ?: stringResource(R.string.zona_titulo),
                        antetitulo = zona?.let { stringResource(textoTipo(it.tipo)) },
                        subtitulo = zona?.subtitulo,
                        onAtras = onAtras,
                    )
                    if (pestanas.size > 1) {
                        SelectorZonas(
                            zonas = pestanas,
                            slugSeleccionado = slugSeleccionado,
                            onZonaClick = onPestanaClick,
                            modifier = Modifier.padding(bottom = Espaciado.l),
                        )
                    }
                }
            },
        ) { zona ->
            item(key = "ilustracion") {
                MarcoIlustracion(url = zona.imagenUrl, descripcion = zona.nombre, semilla = zona.slug)
            }
            item(key = "identidad") { IdentidadZona(zona) }
            item(key = "resumen") {
                zona.resumen?.let { TextoLectura(it, modifier = Modifier.padding(top = Espaciado.s)) }
            }
            item(key = "citas") {
                Column(verticalArrangement = Arrangement.spacedBy(Espaciado.m)) {
                    CitaDestacada(stringResource(R.string.zona_idea_central), zona.ideaCentral)
                    CitaDestacada(stringResource(R.string.zona_tono_emocional), zona.tonoEmocional)
                }
            }

            if (zona.salas.isNotEmpty()) {
                item(key = "salas-titulo") {
                    EtiquetaSeccion(
                        texto = stringResource(R.string.zona_habitaciones),
                        detalle = zona.salas.size.toString().padStart(2, '0'),
                    )
                }
                items(zona.salas, key = { it.slug }) { sala ->
                    Column {
                        FilaIndice(
                            titulo = sala.nombre,
                            numero = sala.orden,
                            onClick = { onSalaClick(sala.slug) },
                            insignias = if (sala.esObservatorio || sala.esZonaSegura) {
                                {
                                    if (sala.esObservatorio) {
                                        Insignia(stringResource(R.string.sala_observatorio), icono = { IconoObservatorio(tamano = 12.dp, color = LocalAcento.current) })
                                    }
                                    if (sala.esZonaSegura) {
                                        Insignia(stringResource(R.string.sala_zona_segura), icono = { IconoZonaSegura(tamano = 12.dp, color = LocalAcento.current) })
                                    }
                                }
                            } else {
                                null
                            },
                        )
                        Divisor()
                    }
                }
            }

            item(key = "exterior") { SeccionLectura(stringResource(R.string.zona_exterior), zona.exterior) }
            item(key = "ambientacion") { SeccionLectura(stringResource(R.string.zona_ambientacion), zona.ambientacion) }
            item(key = "narrativa") { SeccionLectura(stringResource(R.string.zona_narrativa), zona.narrativa) }

            if (zona.mecanicas.isNotEmpty()) {
                item(key = "mecanicas-titulo") { EtiquetaSeccion(stringResource(R.string.zona_mecanicas)) }
                items(zona.mecanicas, key = { it.slug }) { mecanica ->
                    PanelPixel {
                        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m), verticalAlignment = Alignment.CenterVertically) {
                            IconoEngranaje(tamano = 20.dp, color = LocalAcento.current)
                            Text(mecanica.nombre.uppercase(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                        }
                        Text(mecanica.descripcion, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }

            item(key = "coleccionables") { SeccionLectura(stringResource(R.string.zona_coleccionables), zona.coleccionables) }

            if (listOf(zona.paleta, zona.iluminacion, zona.sensacionVisual, zona.tinte).any { !it.isNullOrBlank() }) {
                item(key = "arte-titulo") { EtiquetaSeccion(stringResource(R.string.zona_direccion_arte)) }
                item(key = "arte") {
                    FichaDatos(
                        listOf(
                            stringResource(R.string.zona_paleta) to zona.paleta,
                            stringResource(R.string.zona_iluminacion) to zona.iluminacion,
                            stringResource(R.string.zona_sensacion_visual) to zona.sensacionVisual,
                            stringResource(R.string.zona_tinte) to zona.tinte?.replaceFirstChar { it.uppercase() },
                        )
                    )
                }
            }

            item(key = "restauracion") { SeccionLectura(stringResource(R.string.zona_restauracion), zona.restauracion) }
        }
    }
}

/** Constelación e insignias de la zona (tutorial, sin plataformas) y sus etiquetas. */
@Composable
private fun IdentidadZona(zona: Zona) {
    Column(verticalArrangement = Arrangement.spacedBy(Espaciado.m), modifier = Modifier.padding(top = Espaciado.s)) {
        zona.constelacion?.let { constelacion ->
            Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.s), verticalAlignment = Alignment.CenterVertically) {
                IconoEstrellaGuia(tamano = 18.dp, color = LocalAcento.current)
                Text(
                    text = listOfNotNull(
                        stringResource(R.string.zona_constelacion, constelacion.nombre),
                        constelacion.sobrenombre,
                    ).joinToString(" · ").uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.tertiary,
                )
            }
        }
        if (zona.esTutorial || !zona.esNivel) {
            FilaInsignias {
                if (zona.esTutorial) Insignia(stringResource(R.string.zona_tutorial))
                if (!zona.esNivel) Insignia(stringResource(R.string.zona_sin_plataformas))
            }
        }
        Etiquetas(zona.etiquetas)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1600)
@Composable
private fun ZonaScreenPreview() {
    WikiAstrolabioTheme {
        ZonaScreen(
            uiState = UiState.Exito(DatosPreview.zona),
            pestanas = DatosPreview.zonas,
            slugSeleccionado = DatosPreview.zona.slug,
            onPestanaClick = {},
            onAtras = {},
            onSalaClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
