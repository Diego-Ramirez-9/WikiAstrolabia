package com.equipo.wikiastrolabio.domain.model

/**
 * Respuesta de cualquier repositorio: los datos o un error ya clasificado.
 * La UI nunca recibe excepciones de Retrofit ni de Room.
 */
sealed interface Resultado<out T> {

    data class Exito<out T>(
        val datos: T,
        val origen: OrigenDatos,
    ) : Resultado<T>

    data class Error(val error: ErrorDatos) : Resultado<Nothing>
}

/** De dónde salieron los datos; la UI lo usa para avisar que está sin conexión. */
enum class OrigenDatos {
    /** Recién descargados de la API. */
    RED,

    /** De la caché local, y siguen vigentes (la versión del contenido no cambió). */
    CACHE,

    /** De la caché local porque la API no respondió; pueden estar desactualizados. */
    CACHE_SIN_CONEXION,
}

sealed interface ErrorDatos {
    /** Sin internet, tiempo de espera agotado o la API no responde. */
    data object SinConexion : ErrorDatos

    /** La API respondió 404: el slug o la ruta no existen. */
    data object NoEncontrado : ErrorDatos

    /** La API respondió 5xx. */
    data class Servidor(val codigoHttp: Int, val mensaje: String?) : ErrorDatos

    /** El JSON no tiene la forma esperada (cambió el contrato de la API). */
    data object DatosInvalidos : ErrorDatos

    data class Desconocido(val causa: Throwable) : ErrorDatos
}

inline fun <T, R> Resultado<T>.map(transformar: (T) -> R): Resultado<R> = when (this) {
    is Resultado.Exito -> Resultado.Exito(transformar(datos), origen)
    is Resultado.Error -> this
}
