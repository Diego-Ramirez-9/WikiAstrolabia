package com.equipo.wikiastrolabio.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.ui.theme.Espaciado
import com.equipo.wikiastrolabio.ui.theme.LocalAcento
import com.equipo.wikiastrolabio.ui.theme.PixelifySans

/**
 * Texto de lectura (lore, descripciones, poemas) en serif. Respeta los saltos de párrafo
 * que vienen de la API. Con [capitular], la primera letra se dibuja grande en pixel art,
 * como la letra capitular de un manuscrito.
 */
@Composable
fun TextoLectura(
    texto: String,
    modifier: Modifier = Modifier,
    capitular: Boolean = false,
    cursiva: Boolean = false,
) {
    val parrafos = remember(texto) {
        texto.split(Regex("\\n\\s*\\n|\\n")).map { it.trim() }.filter { it.isNotEmpty() }
    }
    val acento = LocalAcento.current
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(Espaciado.m)) {
        parrafos.forEachIndexed { indice, parrafo ->
            val conCapitular = capitular && indice == 0 && parrafo.length > 1
            Text(
                text = if (!conCapitular) buildAnnotatedString { append(parrafo) } else buildAnnotatedString {
                    withStyle(SpanStyle(fontFamily = PixelifySans, fontSize = 30.sp, color = acento)) {
                        append(parrafo.first())
                    }
                    append(parrafo.drop(1))
                },
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = if (cursiva) FontStyle.Italic else FontStyle.Normal,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

/** Rótulo de sección + texto de lectura. No dibuja nada si la API no mandó el campo. */
@Composable
fun SeccionLectura(
    titulo: String,
    texto: String?,
    modifier: Modifier = Modifier,
    numero: Int? = null,
) {
    if (texto.isNullOrBlank()) return
    Column(modifier = modifier) {
        EtiquetaSeccion(titulo, numero = numero)
        TextoLectura(texto, modifier = Modifier.padding(top = Espaciado.s))
    }
}

/**
 * Frase destacada con barra lateral de acento (idea central, tono emocional, aprendizaje).
 * No dibuja nada si [texto] está vacío.
 */
@Composable
fun CitaDestacada(
    etiqueta: String,
    texto: String?,
    modifier: Modifier = Modifier,
) {
    if (texto.isNullOrBlank()) return
    val acento = LocalAcento.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(acento.copy(alpha = 0.06f), MaterialTheme.shapes.small),
    ) {
        Box(
            modifier = Modifier
                .width(3.dp)
                .fillMaxHeight()
                .background(acento),
        )
        Column(
            modifier = Modifier.padding(Espaciado.l),
            verticalArrangement = Arrangement.spacedBy(Espaciado.xs),
        ) {
            Text(etiqueta.uppercase(), style = MaterialTheme.typography.labelSmall, color = acento)
            Text(
                text = texto,
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
