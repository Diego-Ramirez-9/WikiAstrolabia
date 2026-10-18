package com.equipo.wikiastrolabio.domain.model

/** Resultado de comparar la versión del contenido de la API con la guardada en el teléfono. */
enum class EstadoSincronizacion {
    /** Primera vez o el contenido cambió: la caché se irá renovando al abrir cada pantalla. */
    HAY_CONTENIDO_NUEVO,

    /** Nada cambió: las pantallas usan la caché sin descargar. */
    SIN_CAMBIOS,

    /** No se pudo consultar la API: se trabaja con lo guardado. */
    SIN_CONEXION,
}
