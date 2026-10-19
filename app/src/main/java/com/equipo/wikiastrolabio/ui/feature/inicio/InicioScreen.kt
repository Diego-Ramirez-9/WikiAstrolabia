package com.equipo.wikiastrolabio.ui.feature.inicio

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.equipo.wikiastrolabio.R
import com.equipo.wikiastrolabio.ui.feature.inicio.components.RelojCosmicoDecorativo
import com.equipo.wikiastrolabio.ui.feature.inicio.components.TarjetaCodex
import com.equipo.wikiastrolabio.ui.theme.Background
import com.equipo.wikiastrolabio.ui.theme.Blanco
import com.equipo.wikiastrolabio.ui.theme.Cream200
import com.equipo.wikiastrolabio.ui.theme.MutedPurple600
import com.equipo.wikiastrolabio.ui.theme.PixelifySans
import com.equipo.wikiastrolabio.ui.theme.Sand300
import com.equipo.wikiastrolabio.ui.theme.Slate700
import com.equipo.wikiastrolabio.ui.theme.TerraCotta500
import com.equipo.wikiastrolabio.ui.theme.WikiAstrolabioTheme

/** Secciones del códex que se muestran en Inicio, en este orden. */
enum class SeccionCodex(@param:StringRes val titulo: Int, @param:StringRes val descripcion: Int) {
    HISTORIA(R.string.codex_historia, R.string.codex_historia_desc),
    MUNDO(R.string.codex_mundo, R.string.codex_mundo_desc),
    PERSONAJES(R.string.codex_personajes, R.string.codex_personajes_desc),
    MECANICAS(R.string.codex_mecanicas, R.string.codex_mecanicas_desc),
    ARCHIVOS(R.string.codex_archivos, R.string.codex_archivos_desc),
}

/**
 * Pantalla Inicio rediseñada con Pixelify Sans y esquema técnico del reloj cósmico:
 * - Cabecera retro con "ASTROLABIO" y divisores horizontales
 * - Hero banner con esquema astronómico / reloj cósmico mejorado
 * - Sección "EXPLORA EL CODEX"
 * - Tarjetas con recuadro e íconos pixel-art
 */
@Composable
fun InicioScreen(
    onSeccionClick: (SeccionCodex) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Background),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        // 1. Título principal "ASTROLABIO"
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.inicio_titulo).uppercase(),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = PixelifySans,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.5.sp,
                        fontSize = 28.sp,
                    ),
                    color = TerraCotta500,
                    modifier = Modifier.semantics { heading() },
                )
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(thickness = 1.dp, color = Slate700)
            }
        }

        // 2. Banner Cósmico (Lema + Ilustración del Gran Reloj a la derecha)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
            ) {
                // Esquema técnico del reloj cósmico mejorado
                RelojCosmicoDecorativo(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 10.dp),
                    tamano = 145.dp,
                    colorLineas = Slate700,
                )

                // Textos a la izquierda
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.72f)
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = stringResource(R.string.inicio_registro).uppercase(),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = PixelifySans,
                            fontWeight = FontWeight.Medium,
                            letterSpacing = 1.5.sp,
                            fontSize = 9.5.sp,
                        ),
                        color = MutedPurple600,
                    )

                    Text(
                        text = stringResource(R.string.inicio_lema).uppercase(),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = PixelifySans,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            lineHeight = 24.sp,
                            fontSize = 19.sp,
                        ),
                        color = Blanco,
                    )

                    Text(
                        text = stringResource(R.string.inicio_subtitulo),
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = PixelifySans,
                            fontSize = 12.5.sp,
                            lineHeight = 17.sp,
                            letterSpacing = 0.3.sp,
                        ),
                        color = Sand300,
                    )
                }
            }
        }

        // 3. Separador inferior del banner
        item {
            HorizontalDivider(thickness = 1.dp, color = Slate700)
        }

        // 4. Encabezado de sección "EXPLORA EL CODEX ──────────"
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.inicio_explora).uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontFamily = PixelifySans,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        fontSize = 11.sp,
                    ),
                    color = Cream200,
                )
                HorizontalDivider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 14.dp),
                    thickness = 1.dp,
                    color = Slate700,
                )
            }
        }

        // 5. Lista de las 5 tarjetas de sección del códex con íconos pixel art
        items(SeccionCodex.entries, key = { it.name }) { seccion ->
            TarjetaCodex(
                seccion = seccion,
                titulo = stringResource(seccion.titulo),
                subtitulo = stringResource(seccion.descripcion),
                onClick = { onSeccionClick(seccion) },
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0B151E)
@Composable
private fun InicioScreenPreview() {
    WikiAstrolabioTheme {
        InicioScreen(onSeccionClick = {})
    }
}
