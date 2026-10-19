package com.equipo.wikiastrolabio.ui.preview

import com.equipo.wikiastrolabio.domain.model.Archivo
import com.equipo.wikiastrolabio.domain.model.ArchivoEnSala
import com.equipo.wikiastrolabio.domain.model.ArchivoResumen
import com.equipo.wikiastrolabio.domain.model.CategoriaArchivo
import com.equipo.wikiastrolabio.domain.model.Constelacion
import com.equipo.wikiastrolabio.domain.model.Dialogo
import com.equipo.wikiastrolabio.domain.model.EntradaHistoria
import com.equipo.wikiastrolabio.domain.model.EstadoPersonaje
import com.equipo.wikiastrolabio.domain.model.GrupoRasgos
import com.equipo.wikiastrolabio.domain.model.Habilidad
import com.equipo.wikiastrolabio.domain.model.LineaDialogo
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.MecanicaZona
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Observatorio
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.Rasgo
import com.equipo.wikiastrolabio.domain.model.Referencia
import com.equipo.wikiastrolabio.domain.model.Regla
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.model.TipoHabilidad
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen

/**
 * Datos de ejemplo para las @Preview de Android Studio (no se usan en la app).
 * Permiten diseñar cada pantalla sin emulador y sin API. Textos tomados del seed.
 */
object DatosPreview {

    val historia = listOf(
        SeccionHistoria(
            slug = "cosmologia",
            titulo = "Contexto y cosmología",
            entradas = listOf(
                EntradaHistoria(
                    slug = "el-gran-reloj-cosmico",
                    titulo = "El Gran Reloj Cósmico",
                    contenido = "Una maquinaria colosal suspendida en el vacío: un Eje central del que cuelgan doce Casas, una por cada constelación del zodiaco.",
                    imagenUrl = null,
                ),
                EntradaHistoria(
                    slug = "ofiuco",
                    titulo = "Ofiuco",
                    contenido = "La decimotercera constelación, la del azar y lo impredecible.",
                    imagenUrl = null,
                ),
            ),
        ),
    )

    val mapa = Mapa(
        titulo = "El Gran Reloj",
        descripcion = "Un Eje central del que cuelgan las doce Casas, dispuestas en espiral como las horas de una carátula.",
        imagenUrl = null,
        puntos = listOf(
            PuntoMapa("entranas", "Las Entrañas del Reloj", "Zona de inicio", TipoZona.EJE, "La base de la torre y el tutorial del juego.", 0.5f, 0.88f),
            PuntoMapa("geminis", "Casa de Géminis", "La Casa de los Reflejos", TipoZona.CASA, "Una Casa construida con simetría perfecta que está perdiendo su forma.", 0.22f, 0.48f),
            PuntoMapa("acuario", "Casa de Acuario", "La Casa de las Corrientes", TipoZona.CASA, "Una Casa que se derrama sin control.", 0.78f, 0.48f),
            PuntoMapa("camara-central", "La Cámara Central", "El corazón del Reloj", TipoZona.CIERRE, "El desenlace.", 0.5f, 0.12f),
        ),
    )

    val casas = listOf(
        ZonaResumen("geminis", "Casa de Géminis", "La Casa de los Reflejos", TipoZona.CASA, listOf("Dualidad y reflejos"), null),
        ZonaResumen("acuario", "Casa de Acuario", "La Casa de las Corrientes", TipoZona.CASA, listOf("Corrientes"), null),
    )

    val zona = Zona(
        slug = "geminis",
        nombre = "Casa de Géminis",
        subtitulo = "La Casa de los Reflejos",
        tipo = TipoZona.CASA,
        constelacion = Constelacion("Géminis", "Los Gemelos"),
        etiquetas = listOf("Dualidad y reflejos", "Muros gemelos", "Simetría rota"),
        resumen = "Una Casa construida con simetría perfecta que está perdiendo su forma.",
        exterior = "Dos torres gemelas suspendidas del Eje, unidas entre sí por puentes de latón.",
        ambientacion = "Cada pasillo se refleja a sí mismo.",
        narrativa = "Géminis es la casa de las dos caras de la verdad.",
        restauracion = "Al restaurar la Casa, la mitad oxidada se pule.",
        imagenUrl = null,
        salas = listOf(
            SalaResumen("atrio-de-los-gemelos", "El Atrio de los Gemelos", 1, esZonaSegura = false, esObservatorio = false),
            SalaResumen("sala-observatorio-dos-lentes", "El Observatorio de las Dos Lentes", 2, esZonaSegura = true, esObservatorio = true),
        ),
        mecanicas = listOf(
            MecanicaZona("anclaje-reflejado", "Anclaje reflejado", "Cada anclaje genera una copia invertida en el techo."),
        ),
    )

    val sala = Sala(
        slug = "sala-observatorio-del-cauce",
        nombre = "El Observatorio del Cauce",
        descripcion = "Observatorio mayor y zona de calma antes del final.",
        aprendizaje = null,
        esZonaSegura = true,
        imagenUrl = null,
        zona = Referencia("acuario", "Casa de Acuario"),
        observatorio = Observatorio(
            nombre = "Observatorio del Cauce",
            exterior = "Una pequeña cúpula en forma de ánfora invertida.",
            interior = "Una sala pequeña y seca.",
            luz = null,
            descubrimientos = null,
        ),
        archivos = listOf(ArchivoEnSala("nota-xi-subo-a-terminar", "Última nota del Cauce", "notas-de-xi")),
        dialogos = listOf(
            Dialogo(
                slug = "salida-observatorio-del-cauce",
                titulo = "Al salir del Observatorio del Cauce",
                tipo = "cita",
                momento = null,
                lineas = listOf(LineaDialogo(1, "Ese mecanismo ya no existe. No lo busques. Sigue subiendo.", null, null)),
            ),
        ),
    )

    val personajes = listOf(
        PersonajeResumen("almanaque-xii", "Almanaque XII", RolPersonaje.PROTAGONISTA, "Un autómata pequeño, curioso y silencioso.", null),
        PersonajeResumen("almanaque-xi", "Almanaque XI", RolPersonaje.SECUNDARIO, "El almanaque anterior, relacionado con la ruptura del Gran Reloj.", null),
        PersonajeResumen("efemeride", "Efeméride", RolPersonaje.GUIA, "La voz del Reloj.", null),
    )

    val personaje = Personaje(
        slug = "almanaque-xii",
        nombre = "Almanaque XII",
        titulo = "El duodécimo Almanaque",
        rol = RolPersonaje.PROTAGONISTA,
        estado = EstadoPersonaje.ACTIVO,
        resumen = "Un autómata pequeño, curioso y silencioso.",
        origen = "Es el duodécimo de su linaje.",
        motivacion = "Recolectar los fragmentos de estrella dispersos en cada Casa.",
        personalidad = null,
        arco = null,
        imagenUrl = null,
        rasgos = listOf(
            GrupoRasgos("apariencia", listOf(Rasgo("Capa", "Larga y oscura, cubierta de mapas estelares."))),
            GrupoRasgos("herramienta", listOf(Rasgo("El Astrolabio", "Un disco de latón con anillos concéntricos."))),
        ),
        dialogos = emptyList(),
    )

    val mecanicas = Mecanicas(
        habilidades = listOf(
            Habilidad("salto", 1, "Salto", "Salto", "Altura variable", TipoHabilidad.MECANICA, "Salto base; altura variable.", null, null, mapOf("altura" to "variable"), null),
            Habilidad("punto-de-anclaje", 2, "Ancla", "Punto de anclaje", "6 u · 5 s", TipoHabilidad.MECANICA, "Fija una estrella temporal dentro de 6 unidades.", null, null, mapOf("duracion_s" to "5"), null),
            Habilidad("descansar", 8, "Descansar", "Descansar", null, TipoHabilidad.NARRATIVA, "En los observatorios se sienta y da cuerda a su llave.", null, null, emptyMap(), null),
        ),
        reglas = listOf(Regla("recarga", "Recarga", "Tocar suelo firme recarga los usos del gancho.")),
    )

    val categoriasArchivo = listOf(
        CategoriaArchivo(
            slug = "fragmentos-de-estrella",
            nombre = "Fragmentos de Estrella",
            descripcion = "Espacio preparado para los registros oficiales de esta colección.",
            iconoUrl = null,
            elementos = emptyList(),
        ),
        CategoriaArchivo(
            slug = "poemas",
            nombre = "Poemas",
            descripcion = "Ecos que suben desde la Tierra hasta la torre.",
            iconoUrl = null,
            elementos = listOf(
                ArchivoResumen("poema-gemelas", "Gemelas", "Los Puentes Paralelos", null),
                ArchivoResumen("poema-cantaro", "Cántaro", "Los Acueductos", null),
            ),
        ),
        CategoriaArchivo("notas-de-xi", "Notas de XI", null, null, emptyList()),
    )

    val archivo = Archivo(
        slug = "poema-gemelas",
        titulo = "Gemelas",
        categoria = "poemas",
        contenido = "Mi hermana y yo nacimos bajo la misma estrella y a las dos nos tocó la misma vida.",
        autor = "Anónimo (desde la Tierra)",
        ubicacion = "Los Puentes Paralelos",
        imagenUrl = null,
        zona = Referencia("geminis", "Casa de Géminis"),
        sala = Referencia("puentes-paralelos", "Los Puentes Paralelos"),
    )
}
