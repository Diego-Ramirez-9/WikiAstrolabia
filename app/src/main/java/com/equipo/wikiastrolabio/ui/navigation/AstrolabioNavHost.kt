package com.equipo.wikiastrolabio.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.equipo.wikiastrolabio.ui.feature.archivos.ArchivoDetalleRoute
import com.equipo.wikiastrolabio.ui.feature.archivos.ArchivosRoute
import com.equipo.wikiastrolabio.ui.feature.historia.EntradaHistoriaRoute
import com.equipo.wikiastrolabio.ui.feature.historia.HistoriaRoute
import com.equipo.wikiastrolabio.ui.feature.inicio.InicioScreen
import com.equipo.wikiastrolabio.ui.feature.inicio.SeccionCodex
import com.equipo.wikiastrolabio.ui.feature.mecanicas.MecanicasRoute
import com.equipo.wikiastrolabio.ui.feature.mundo.MundoRoute
import com.equipo.wikiastrolabio.ui.feature.personajes.PersonajeDetalleRoute
import com.equipo.wikiastrolabio.ui.feature.personajes.PersonajesRoute
import com.equipo.wikiastrolabio.ui.feature.salas.SalaRoute
import com.equipo.wikiastrolabio.ui.feature.zonas.ZonaRoute

/**
 * Mapa completo de la navegación. Cada pestaña de la barra inferior es un grafo anidado
 * con su propia pila, así la pestaña seleccionada se calcula sola (ver DestinoPrincipal).
 *
 * Las pantallas no conocen al NavController: reciben lambdas (onAtras, onXClick) y aquí se
 * decide a dónde lleva cada una. Así se pueden previsualizar y probar por separado.
 */
@Composable
fun AstrolabioNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val onAtras: () -> Unit = { navController.navigateUp() }
    val abrirArchivo: (String) -> Unit = { slug -> navController.navigate(Ruta.ArchivoDetalle(slug)) }
    val abrirSala: (String) -> Unit = { slug -> navController.navigate(Ruta.SalaDetalle(slug)) }

    NavHost(
        navController = navController,
        startDestination = Ruta.PestanaInicio,
        modifier = modifier,
    ) {
        navigation<Ruta.PestanaInicio>(startDestination = Ruta.Inicio) {
            composable<Ruta.Inicio> {
                InicioScreen(
                    onSeccionClick = { seccion ->
                        when (seccion) {
                            SeccionCodex.HISTORIA -> navController.navigate(Ruta.Historia)
                            SeccionCodex.MUNDO -> navController.navegarAPestana(DestinoPrincipal.MUNDO)
                            SeccionCodex.PERSONAJES -> navController.navigate(Ruta.Personajes)
                            SeccionCodex.MECANICAS -> navController.navigate(Ruta.Mecanicas)
                            SeccionCodex.ARCHIVOS -> navController.navegarAPestana(DestinoPrincipal.ARCHIVOS)
                        }
                    },
                )
            }
            composable<Ruta.Historia> {
                HistoriaRoute(
                    onAtras = onAtras,
                    onEntradaClick = { slug -> navController.navigate(Ruta.EntradaHistoriaDetalle(slug)) },
                )
            }
            composable<Ruta.EntradaHistoriaDetalle> { EntradaHistoriaRoute(onAtras = onAtras) }
            composable<Ruta.Personajes> {
                PersonajesRoute(
                    onAtras = onAtras,
                    onPersonajeClick = { slug -> navController.navigate(Ruta.PersonajeDetalle(slug)) },
                )
            }
            composable<Ruta.PersonajeDetalle> { PersonajeDetalleRoute(onAtras = onAtras) }
            composable<Ruta.Mecanicas> { MecanicasRoute(onAtras = onAtras) }
        }

        navigation<Ruta.PestanaMundo>(startDestination = Ruta.Mundo) {
            composable<Ruta.Mundo> {
                MundoRoute(
                    onAtras = onAtras,
                    onExplorarZona = { slug -> navController.navigate(Ruta.ZonaDetalle(slug)) },
                )
            }
            composable<Ruta.ZonaDetalle> { ZonaRoute(onAtras = onAtras, onSalaClick = abrirSala) }
            composable<Ruta.SalaDetalle> { SalaRoute(onAtras = onAtras, onArchivoClick = abrirArchivo) }
        }

        navigation<Ruta.PestanaArchivos>(startDestination = Ruta.Archivos) {
            composable<Ruta.Archivos> { ArchivosRoute(onAtras = onAtras, onArchivoClick = abrirArchivo) }
            composable<Ruta.ArchivoDetalle> { ArchivoDetalleRoute(onAtras = onAtras, onSalaClick = abrirSala) }
        }
    }
}
