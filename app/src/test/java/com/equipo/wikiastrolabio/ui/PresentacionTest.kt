package com.equipo.wikiastrolabio.ui

import com.equipo.wikiastrolabio.ui.feature.mecanicas.components.etiquetaParametro
import org.junit.Assert.assertEquals
import org.junit.Test

/** Reglas de presentación que no dependen de Android: etiquetas legibles de los parámetros. */
class PresentacionTest {

    @Test
    fun `las claves de los parametros se vuelven etiquetas legibles`() {
        assertEquals("Duración (s)", etiquetaParametro("duracion_s"))
        assertEquals("Duración máx. (s)", etiquetaParametro("duracion_s_max"))
        assertEquals("Máx. activos", etiquetaParametro("max_activos"))
        assertEquals("Alcance (u)", etiquetaParametro("alcance_unidades"))
        assertEquals("Velocidad viento", etiquetaParametro("velocidad_viento"))
    }
}
