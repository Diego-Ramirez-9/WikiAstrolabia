package com.equipo.wikiastrolabio.ui.feature.salas

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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.Observatorio
import com.equipo.wikiastrolabio.domain.model.RecorridoSala
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.common.acentoDeZona
import com.equipo.wikiastrolabio.ui.components.BloqueDialogo
import com.equipo.wikiastrolabio.ui.components.ChipEnlace
import com.equipo.wikiastrolabio.ui.components.CitaDestacada
import com.equipo.wikiastrolabio.ui.components.Divisor
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.FichaDatos
import com.equipo.wikiastrolabio.ui.components.FilaIndice
import com.equipo.wikiastrolabio.ui.components.FilaInsignias
import com.equipo.wikiastrolabio.ui.components.Insignia
import com.equipo.wikiastrolabio.ui.components.MarcoIlustracion
import com.equipo.wikiastrolabio.ui.components.NavegacionVecinos
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.TextoLectura
import com.equipo.wikiastrolabio.ui.components.Vecino
import com.equipo.wikiastrolabio.ui.components.pixel.IconoObservatorio
import com.equipo.wikiastrolabio.ui.components.pixel.IconoPoema
import com.equipo.wikiastrolabio.ui.components.pixel.IconoZonaSegura
import com.equipo.wikiastrolabio.ui.feature.archivos.components.textoCategoria
import com.equipo.wikiastrolabio.ui.preview.DatosPreview
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.ConAcento
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

@Composable
fun SalaRoute(
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onZonaClick: (slug: String) -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onPersonajeClick: (slug: String) -> Unit,
    viewModel: SalaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SalaScreen(
        uiState = uiState,
        onAtras = onAtras,
        onSalaClick = onSalaClick,
        onZonaClick = onZonaClick,
        onArchivoClick = onArchivoClick,
        onPersonajeClick = onPersonajeClick,
        onReintentar = viewModel::reintentar,
        onRecargar = viewModel::recargar,
    )
}

/**
 * Una sala del recorrido: su lugar en la zona, descripción, lo que se aprende, el observatorio
 * (si lo tiene), los archivos que se encuentran ahí, los diálogos y el paso a la sala vecina.
 */
@Composable
fun SalaScreen(
    uiState: UiState<RecorridoSala>,
    onAtras: () -> Unit,
    onSalaClick: (slug: String) -> Unit,
    onZonaClick: (slug: String) -> Unit,
    onArchivoClick: (slug: String) -> Unit,
    onPersonajeClick: (slug: String) -> Unit,
    onReintentar: () -> Unit,
    onRecargar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recorridoActual = (uiState as? UiState.Exito)?.datos
    val acento = recorridoActual?.let { acentoDeZona(it.tipoZona, it.tinteZona) } ?: Ambar400

    ConAcento(acento) {
        PantallaContenido(
            uiState = uiState,
            onReintentar = onReintentar,
            onRecargar = onRecargar,
            modifier = modifier,
            encabezado = { recorrido ->
                val numero = recorrido?.sala?.orden?.toString()?.padStart(2, '0').orEmpty()
                EncabezadoPantalla(
                    titulo = recorrido?.sala?.nombre.orEmpty(),
                    antetitulo = when {
                        recorrido == null -> null
                        recorrido.total > 0 ->
                            stringResource(R.string.sala_posicion, numero, recorrido.total.toString().padStart(2, '0'))
                        else -> stringResource(R.string.sala_posicion_simple, numero)
                    },
                    onAtras = onAtras,
                )
            },
        ) { recorrido ->
            val sala = recorrido.sala
            item(key = "contexto") {
                FilaInsignias {
                    sala.zona?.let { zona ->
                        ChipEnlace(stringResource(R.string.sala_ver_zona, zona.nombre), onClick = { onZonaClick(zona.slug) })
                    }
                    if (sala.observatorio != null) {
                        Insignia(stringResource(R.string.sala_observatorio), icono = { IconoObservatorio(tamano = 12.dp, color = LocalAcento.current) })
                    }
                    if (sala.esZonaSegura) {
                        Insignia(stringResource(R.string.sala_zona_segura), icono = { IconoZonaSegura(tamano = 12.dp, color = LocalAcento.current) })
                    }
                }
            }
            item(key = "ilustracion") {
                MarcoIlustracion(url = sala.imagenUrl, descripcion = sala.nombre, semilla = sala.slug)
            }
            item(key = "descripcion") {
                sala.descripcion?.let { TextoLectura(it, modifier = Modifier.padding(top = Espaciado.s)) }
            }
            item(key = "aprendizaje") { CitaDestacada(stringResource(R.string.sala_aprendizaje), sala.aprendizaje) }

            sala.observatorio?.let { observatorio ->
                item(key = "observatorio-titulo") { EtiquetaSeccion(stringResource(R.string.sala_observatorio_mayor)) }
                item(key = "observatorio") { TarjetaObservatorio(observatorio) }
                item(key = "observatorio-ficha") {
                    FichaDatos(
                        listOf(
                            stringResource(R.string.observatorio_exterior) to observatorio.exterior,
                            stringResource(R.string.observatorio_interior) to observatorio.interior,
                            stringResource(R.string.observatorio_luz) to observatorio.luz,
                            stringResource(R.string.observatorio_audio) to observatorio.audio,
                            stringResource(R.string.observatorio_descubrimientos) to observatorio.descubrimientos,
                        )
                    )
                }
            }

            if (sala.archivos.isNotEmpty()) {
                item(key = "archivos-titulo") {
                    EtiquetaSeccion(stringResource(R.string.sala_archivos), detalle = sala.archivos.size.toString().padStart(2, '0'))
                }
                items(sala.archivos, key = { "archivo-${it.slug}" }) { archivo ->
                    Column {
                        FilaIndice(
                            titulo = archivo.titulo,
                            subtitulo = textoCategoria(archivo.categoria),
                            onClick = { onArchivoClick(archivo.slug) },
                            inicio = { IconoPoema(tamano = 20.dp, color = LocalAcento.current) },
                        )
                        Divisor()
                    }
                }
            }

            if (sala.dialogos.isNotEmpty()) {
                item(key = "dialogos-titulo") { EtiquetaSeccion(stringResource(R.string.dialogos_titulo)) }
                items(sala.dialogos, key = { "dialogo-${it.slug}" }) { dialogo ->
                    BloqueDialogo(dialogo, onPersonajeClick = onPersonajeClick)
                }
            }

            item(key = "vecinas") {
                NavegacionVecinos(
                    anterior = recorrido.anterior?.let {
                        Vecino(stringResource(R.string.sala_anterior), it.nombre) { onSalaClick(it.slug) }
                    },
                    siguiente = recorrido.siguiente?.let {
                        Vecino(stringResource(R.string.sala_siguiente), it.nombre) { onSalaClick(it.slug) }
                    },
                )
            }
        }
    }
}

@Composable
private fun TarjetaObservatorio(observatorio: Observatorio) {
    PanelPixel(resaltado = true) {
        Row(horizontalArrangement = Arrangement.spacedBy(Espaciado.m), verticalAlignment = Alignment.CenterVertically) {
            IconoObservatorio(tamano = 32.dp, color = LocalAcento.current)
            Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xxs)) {
                Text(observatorio.nombre.uppercase(), style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
                observatorio.ubicacion?.let {
                    Text(it.uppercase(), style = MaterialTheme.typography.labelSmall, color = LocalAcento.current)
                }
            }
        }
        observatorio.contenidoBreve?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium, fontStyle = FontStyle.Italic, color = MaterialTheme.colorScheme.tertiary)
        }
        observatorio.resumen?.let {
            Text(it, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1800)
@Composable
private fun SalaScreenPreview() {
    WikiAstrolabioTheme {
        SalaScreen(
            uiState = UiState.Exito(DatosPreview.sala),
            onAtras = {},
            onSalaClick = {},
            onZonaClick = {},
            onArchivoClick = {},
            onPersonajeClick = {},
            onReintentar = {},
            onRecargar = {},
        )
    }
}
