package com.equipo.wikiastrolabio.testutil

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaDao
import com.equipo.wikiastrolabio.data.local.cache.CacheRespuestaEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/** DAO de Room en memoria para probar la lógica de caché sin base de datos. */
class CacheRespuestaDaoFalso : CacheRespuestaDao {
    val filas = mutableMapOf<String, CacheRespuestaEntity>()

    override suspend fun obtener(clave: String) = filas[clave]

    override suspend fun guardar(respuesta: CacheRespuestaEntity) {
        filas[respuesta.clave] = respuesta
    }

    override suspend fun eliminar(clave: String) {
        filas.remove(clave)
    }

    override suspend fun vaciar() = filas.clear()
}

/** DataStore en memoria (sin archivos) para PreferenciasContenido. */
class DataStoreEnMemoria : DataStore<Preferences> {
    private val estado = MutableStateFlow(emptyPreferences())

    override val data: Flow<Preferences> = estado

    override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
        val nuevo = transform(estado.value)
        estado.value = nuevo
        return nuevo
    }
}
