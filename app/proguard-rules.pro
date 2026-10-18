# Reglas de R8 para el build release (isMinifyEnabled = true).
#
# Retrofit 3, OkHttp 5, kotlinx.serialization, Room, Hilt y Coil ya traen sus propias reglas
# dentro de sus librerías, así que aquí solo va lo específico de este proyecto.

# Conserva números de línea en los reportes de errores, sin exponer el nombre del archivo.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# Retrofit lee los tipos genéricos de AstrolabioApi (ej. RespuestaApi<List<PersonajeDto>>)
# por reflexión; R8 en modo completo puede borrar esa información si no se conserva.
-keep,allowobfuscation,allowshrinking interface com.equipo.wikiastrolabio.data.remote.AstrolabioApi
-keep,allowobfuscation,allowshrinking class com.equipo.wikiastrolabio.data.remote.dto.RespuestaApi
