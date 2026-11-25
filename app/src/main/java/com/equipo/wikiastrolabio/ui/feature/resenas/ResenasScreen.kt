package com.equipo.wikiastrolabio.ui.feature.resenas

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyReceiver
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.ReglasResena
import com.equipo.wikiastrolabio.domain.model.Resena
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import com.equipo.wikiastrolabio.ui.common.UiState
import com.equipo.wikiastrolabio.ui.components.BotonPixel
import com.equipo.wikiastrolabio.ui.components.EncabezadoPantalla
import com.equipo.wikiastrolabio.ui.components.EtiquetaSeccion
import com.equipo.wikiastrolabio.ui.components.PanelPixel
import com.equipo.wikiastrolabio.ui.components.PantallaContenido
import com.equipo.wikiastrolabio.ui.components.pixel.EstrellaPixel
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ResenasRoute(
    onAtras: () -> Unit,
    viewModel: ResenasViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val formulario by viewModel.formulario.collectAsStateWithLifecycle()

    ResenasScreen(
        uiState = uiState,
        formulario = formulario,
        acciones = AccionesResenas(
            onAtras = onAtras,
            onReintentar = viewModel::reintentar,
            onRecargar = viewModel::recargar,
            onApodo = viewModel::cambiarApodo,
            onCalificacion = viewModel::cambiarCalificacion,
            onTexto = viewModel::cambiarTexto,
            onPublicar = viewModel::publicar,
        ),
    )
}

/** Todo lo que la pantalla puede pedir; se agrupa para no pasar siete lambdas sueltas. */
data class AccionesResenas(
    val onAtras: () -> Unit = {},
    val onReintentar: () -> Unit = {},
    val onRecargar: () -> Unit = {},
    val onApodo: (String) -> Unit = {},
    val onCalificacion: (Int) -> Unit = {},
    val onTexto: (String) -> Unit = {},
    val onPublicar: () -> Unit = {},
)

/**
 * Reseñas del juego: promedio, formulario para escribir una (sin cuenta, solo un apodo) y las
 * más recientes.
 */
@Composable
fun ResenasScreen(
    uiState: UiState<ResumenResenas>,
    formulario: FormularioResena,
    acciones: AccionesResenas,
    modifier: Modifier = Modifier,
) {
    PantallaContenido(
        uiState = uiState,
        onReintentar = acciones.onReintentar,
        onRecargar = acciones.onRecargar,
        modifier = modifier.imePadding(),
        encabezado = {
            EncabezadoPantalla(
                titulo = stringResource(R.string.resenas_titulo),
                antetitulo = stringResource(R.string.resenas_antetitulo),
                subtitulo = stringResource(R.string.resenas_subtitulo),
                onAtras = acciones.onAtras,
                principal = true,
            )
        },
    ) { resumen ->
        item(key = "resumen") { PanelResumen(resumen) }
        item(key = "formulario-titulo") { EtiquetaSeccion(stringResource(R.string.resenas_escribe), numero = 1) }
        item(key = "formulario") { PanelFormulario(formulario, acciones) }
        item(key = "lista-titulo") { EtiquetaSeccion(stringResource(R.string.resenas_opiniones), numero = 2) }
        if (resumen.resenas.isEmpty()) {
            item(key = "vacio") {
                Text(
                    text = stringResource(R.string.resenas_vacio),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        items(resumen.resenas, key = { it.id }) { resena ->
            TarjetaResena(resena)
        }
    }
}

@Composable
private fun PanelResumen(resumen: ResumenResenas) {
    val acento = LocalAcento.current
    PanelPixel(resaltado = true, relleno = PaddingValues(Espaciado.l)) {
        val promedio = resumen.promedio
        if (promedio == null || resumen.total == 0) {
            Text(
                text = stringResource(R.string.resenas_sin_resenas).uppercase(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text = stringResource(R.string.resenas_se_el_primero),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        } else {
            val promedioTexto = String.format(Locale.forLanguageTag("es-MX"), "%.1f", promedio)
            val descripcion = stringResource(R.string.resenas_promedio_descripcion, promedioTexto)
            Row(
                horizontalArrangement = Arrangement.spacedBy(Espaciado.l),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = descripcion
                },
            ) {
                Text(promedioTexto, style = MaterialTheme.typography.displaySmall, color = acento)
                Column(verticalArrangement = Arrangement.spacedBy(Espaciado.xs)) {
                    FilaEstrellas(promedio.roundToInt(), tamano = 18.dp)
                    Text(
                        text = pluralStringResource(R.plurals.resenas_total, resumen.total, resumen.total),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}

@Composable
private fun PanelFormulario(formulario: FormularioResena, acciones: AccionesResenas) {
    val acento = LocalAcento.current
    val colores = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = acento,
        focusedLabelColor = acento,
        cursorColor = acento,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
    )
    PanelPixel(separacion = Espaciado.m) {
        Text(
            text = stringResource(R.string.resenas_calificacion).uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.tertiary,
        )
        SelectorEstrellas(formulario.calificacion, onCambio = acciones.onCalificacion)

        val largoApodo = ReglasResena.longitud(formulario.apodo)
        OutlinedTextField(
            value = formulario.apodo,
            onValueChange = acciones.onApodo,
            label = { Text(stringResource(R.string.resenas_apodo)) },
            singleLine = true,
            isError = largoApodo > ReglasResena.APODO.last,
            supportingText = { Contador(largoApodo, ReglasResena.APODO.last) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words, imeAction = ImeAction.Next),
            colors = colores,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
        )

        val largoTexto = ReglasResena.longitud(formulario.texto)
        OutlinedTextField(
            value = formulario.texto,
            onValueChange = acciones.onTexto,
            label = { Text(stringResource(R.string.resenas_texto)) },
            placeholder = { Text(stringResource(R.string.resenas_texto_ayuda)) },
            minLines = 4,
            maxLines = 10,
            isError = largoTexto > ReglasResena.TEXTO.last,
            supportingText = { Contador(largoTexto, ReglasResena.TEXTO.last, minimo = ReglasResena.TEXTO.first) },
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            colors = colores,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
        )

        Text(
            text = stringResource(R.string.resenas_aviso_publico),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        formulario.error?.let { error ->
            Text(
                text = (error as? ErrorDatos.Rechazado)?.mensaje ?: stringResource(error.mensajeEnvio()),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.semantics { liveRegionPolite() },
            )
        }
        if (formulario.publicada) {
            Text(
                text = stringResource(R.string.resenas_publicada),
                style = MaterialTheme.typography.bodySmall,
                color = acento,
                modifier = Modifier.semantics { liveRegionPolite() },
            )
        }

        BotonPixel(
            texto = stringResource(if (formulario.enviando) R.string.resenas_publicando else R.string.resenas_publicar),
            onClick = acciones.onPublicar,
            habilitado = formulario.puedeEnviar,
            conFlecha = false,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun SemanticsPropertyReceiver.liveRegionPolite() {
    liveRegion = LiveRegionMode.Polite
}

/** "120 / 500"; en rojo si se pasa del máximo y con el mínimo como pista mientras no lo alcanza. */
@Composable
private fun Contador(largo: Int, maximo: Int, minimo: Int? = null) {
    val texto = if (minimo != null && largo < minimo) {
        stringResource(R.string.resenas_minimo, minimo)
    } else {
        "$largo / $maximo"
    }
    Text(
        text = texto,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.End,
        color = if (largo > maximo) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

/** Cinco estrellas tocables (48 dp cada una) para elegir la calificación. */
@Composable
private fun SelectorEstrellas(calificacion: Int, onCambio: (Int) -> Unit) {
    val acento = LocalAcento.current
    Row(Modifier.selectableGroup()) {
        ReglasResena.CALIFICACION.forEach { valor ->
            val descripcion = pluralStringResource(R.plurals.resenas_estrellas, valor, valor)
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(Espaciado.objetivoTactil)
                    .selectable(
                        selected = calificacion == valor,
                        onClick = { onCambio(valor) },
                        role = Role.RadioButton,
                    )
                    .semantics { contentDescription = descripcion },
            ) {
                EstrellaPixel(seleccionada = valor <= calificacion, color = acento, tamano = 30.dp)
            }
        }
    }
}

@Composable
private fun FilaEstrellas(calificacion: Int, tamano: Dp) {
    val acento = LocalAcento.current
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        ReglasResena.CALIFICACION.forEach { valor ->
            EstrellaPixel(seleccionada = valor <= calificacion, color = acento, tamano = tamano)
        }
    }
}

@Composable
private fun TarjetaResena(resena: Resena) {
    PanelPixel {
        val estrellas = pluralStringResource(R.plurals.resenas_estrellas, resena.calificacion, resena.calificacion)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Espaciado.s),
        ) {
            Text(
                text = resena.apodo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
            )
            Box(Modifier.clearAndSetSemantics { contentDescription = estrellas }) {
                FilaEstrellas(resena.calificacion, tamano = 14.dp)
            }
        }
        resena.creadoEn?.let { fecha ->
            Text(
                text = formatearFecha(fecha),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = resena.texto,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val formatoFecha = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es-MX"))

private fun formatearFecha(fecha: Instant): String = formatoFecha.format(fecha.atZone(ZoneId.systemDefault()))

/** Mensaje para un envío fallido cuando la API no mandó uno propio. */
@StringRes
private fun ErrorDatos.mensajeEnvio(): Int = when (this) {
    ErrorDatos.SinConexion -> R.string.resenas_error_sin_conexion
    is ErrorDatos.Servidor -> R.string.error_servidor
    else -> R.string.resenas_error_envio
}

private val resenasPreview = ResumenResenas(
    total = 2,
    promedio = 4.5,
    resenas = listOf(
        Resena("1", "Viajera", 5, "Me encantó la historia del Almanaque y el gancho se siente increíble.", Instant.parse("2026-11-25T17:45:31Z")),
        Resena("2", "Engranaje", 4, "La Casa de Géminis es preciosa, aunque el final me dejó con ganas de más.", Instant.parse("2026-11-24T10:00:00Z")),
    ),
)

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 1500)
@Composable
private fun ResenasScreenPreview() {
    WikiAstrolabioTheme {
        ResenasScreen(
            uiState = UiState.Exito(resenasPreview),
            formulario = FormularioResena(apodo = "Viajera", calificacion = 4, texto = "Muy buen juego"),
            acciones = AccionesResenas(),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0A131C, heightDp = 900)
@Composable
private fun ResenasVaciasPreview() {
    WikiAstrolabioTheme {
        ResenasScreen(
            uiState = UiState.Exito(ResumenResenas(total = 0, promedio = null, resenas = emptyList())),
            formulario = FormularioResena(error = ErrorDatos.Rechazado(429, "Ya publicaste una reseña hace poco.")),
            acciones = AccionesResenas(),
        )
    }
}
