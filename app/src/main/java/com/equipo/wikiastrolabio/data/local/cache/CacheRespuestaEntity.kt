package com.equipo.wikiastrolabio.data.local.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Copia guardada de una respuesta de la API (modo offline).
 *
 * Se guarda el JSON completo de cada endpoint en lugar de normalizarlo en una tabla por
 * entidad: la API ya entrega cada pantalla armada, el contenido es pequeño y de solo
 * lectura, y así un cambio en el contrato de la API no obliga a migrar la base de datos.
 */
@Entity(tableName = "cache_respuestas")
data class CacheRespuestaEntity(
    /** Ruta del endpoint relativa a /api/v1, ej. "personajes/almanaque-xii" o "zonas?tipo=casa". */
    @PrimaryKey val clave: String,
    /** DTO serializado con kotlinx.serialization. */
    val json: String,
    /** Valor de /contenido/version cuando se descargó; si ya no coincide, la copia está vieja. */
    @ColumnInfo(name = "version_contenido") val versionContenido: String?,
    @ColumnInfo(name = "guardado_en") val guardadoEn: Long,
)
