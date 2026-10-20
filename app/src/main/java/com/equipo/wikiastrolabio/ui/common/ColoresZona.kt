package com.equipo.wikiastrolabio.ui.common

import androidx.compose.ui.graphics.Color
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.ui.theme.Ambar400
import com.equipo.wikiastrolabio.ui.theme.Terracota500
import com.equipo.wikiastrolabio.ui.theme.TinteLaton
import com.equipo.wikiastrolabio.ui.theme.colorDeTinte

/** Color de cada tipo de zona: latón para el Eje, ámbar para las Casas, cobre para el desenlace. */
fun colorDeTipo(tipo: TipoZona): Color = when (tipo) {
    TipoZona.EJE -> TinteLaton
    TipoZona.CASA -> Ambar400
    TipoZona.CIERRE -> Terracota500
    TipoZona.DESCONOCIDO -> Ambar400
}

/** Acento de una zona: su tinte de la API ("violeta", "turquesa"…) o, si no tiene, el de su tipo. */
fun acentoDeZona(tipo: TipoZona, tinte: String?): Color =
    if (tinte.isNullOrBlank()) colorDeTipo(tipo) else colorDeTinte(tinte)
