package com.equipo.wikiastrolabio.data.local.cache

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CacheRespuestaDao {

    @Query("SELECT * FROM cache_respuestas WHERE clave = :clave")
    suspend fun obtener(clave: String): CacheRespuestaEntity?

    @Upsert
    suspend fun guardar(respuesta: CacheRespuestaEntity)

    @Query("DELETE FROM cache_respuestas WHERE clave = :clave")
    suspend fun eliminar(clave: String)

    @Query("DELETE FROM cache_respuestas")
    suspend fun vaciar()
}
