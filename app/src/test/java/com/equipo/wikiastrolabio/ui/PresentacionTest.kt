package com.equipo.wikiastrolabio.ui

import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.common.acentoDeZona
import com.equipo.wikiastrolabio.ui.common.colorDeTipo
import com.equipo.wikiastrolabio.ui.feature.mecanicas.components.etiquetaParametro
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.TinteTurquesa
import com.equipo.wikiastrolabio.ui.theme.TinteVioleta
import com.equipo.wikiastrolabio.ui.theme.colorDeTinte
import org.junit.Assert.assertEquals
import org.junit.Test

/** Reglas de presentación que no dependen de Android: etiquetas legibles y colores de zona. */
class PresentacionTest {

    @Test
    fun `las claves de los parametros se vuelven etiquetas legibles`() {
        assertEquals("Duración (s)", etiquetaParametro("duracion_s"))
        assertEquals("Duración máx. (s)", etiquetaParametro("duracion_s_max"))
        assertEquals("Máx. activos", etiquetaParametro("max_activos"))
        assertEquals("Alcance (u)", etiquetaParametro("alcance_unidades"))
        assertEquals("Velocidad viento", etiquetaParametro("velocidad_viento"))
    }

    @Test
    fun `el tinte de la API decide el acento de la zona`() {
        assertEquals(TinteVioleta, colorDeTinte("Violeta"))
        assertEquals(TinteTurquesa, colorDeTinte(" turquesa "))
        assertEquals(Ambar400, colorDeTinte("color-nuevo"))
        assertEquals(TinteVioleta, acentoDeZona(TipoZona.CASA, "violeta"))
        assertEquals(colorDeTipo(TipoZona.EJE), acentoDeZona(TipoZona.EJE, null))
    }
}
