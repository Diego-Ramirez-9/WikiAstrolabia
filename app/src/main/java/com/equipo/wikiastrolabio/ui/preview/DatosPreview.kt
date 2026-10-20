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
import com.equipo.wikiastrolabio.domain.model.LecturaHistoria
import com.equipo.wikiastrolabio.domain.model.LineaDialogo
import com.equipo.wikiastrolabio.domain.model.Mapa
import com.equipo.wikiastrolabio.domain.model.MecanicaZona
import com.equipo.wikiastrolabio.domain.model.Mecanicas
import com.equipo.wikiastrolabio.domain.model.Observatorio
import com.equipo.wikiastrolabio.domain.model.Personaje
import com.equipo.wikiastrolabio.domain.model.PersonajeResumen
import com.equipo.wikiastrolabio.domain.model.PuntoMapa
import com.equipo.wikiastrolabio.domain.model.Rasgo
import com.equipo.wikiastrolabio.domain.model.RecorridoSala
import com.equipo.wikiastrolabio.domain.model.Referencia
import com.equipo.wikiastrolabio.domain.model.Regla
import com.equipo.wikiastrolabio.domain.model.RolPersonaje
import com.equipo.wikiastrolabio.domain.model.Sala
import com.equipo.wikiastrolabio.domain.model.SalaResumen
import com.equipo.wikiastrolabio.domain.model.SeccionHistoria
import com.equipo.wikiastrolabio.domain.model.SeccionResumen
import com.equipo.wikiastrolabio.domain.model.TipoDialogo
import com.equipo.wikiastrolabio.domain.model.TipoHabilidad
import com.equipo.wikiastrolabio.domain.model.TipoZona
import com.equipo.wikiastrolabio.domain.model.Zona
import com.equipo.wikiastrolabio.domain.model.ZonaResumen

/**
 * Datos de ejemplo SOLO para las @Preview de Android Studio: nunca se muestran en la app,
 * que usa únicamente lo que manda la API. Los textos son extractos del contenido real.
 */
object DatosPreview {

    private val gemelos = EntradaHistoria(
        slug = "el-gran-reloj-cosmico",
        titulo = "El Gran Reloj Cósmico",
        contenido = "Una maquinaria colosal suspendida en el vacío: un Eje central del que cuelgan doce Casas, una por cada constelación del zodiaco.\n\nMueve las estrellas, y las estrellas escriben el destino de cada ser vivo con una exactitud enfermiza.",
        imagenUrl = null,
        seccion = SeccionResumen("cosmologia", "Contexto y cosmología"),
    )

    val historia = listOf(
        SeccionHistoria(
            slug = "cosmologia",
            titulo = "Contexto y cosmología",
            entradas = listOf(
                EntradaHistoria("los-creadores", "Los creadores", "Hace eones, una civilización temía tanto a la incertidumbre que construyó el Gran Reloj Cósmico.", null),
                gemelos,
                EntradaHistoria("ofiuco", "Ofiuco", "La decimotercera constelación, la del azar y lo impredecible.", null),
            ),
        ),
        SeccionHistoria(
            slug = "evento",
            titulo = "El evento detonante",
            entradas = listOf(EntradaHistoria("el-escape-de-la-luz", "El escape de la luz", "Ninguna prisión es perfecta.", null)),
        ),
    )

    val lectura = LecturaHistoria(
        entrada = gemelos,
        numeroCapitulo = 1,
        anterior = historia.first().entradas[0],
        siguiente = historia.first().entradas[2],
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

    val zonas = mapa.puntos.map { ZonaResumen(it.slug, it.nombre, it.subtitulo, it.tipo, emptyList(), null) }

    val zona = Zona(
        slug = "geminis",
        nombre = "Casa de Géminis",
        subtitulo = "La Casa de los Reflejos",
        tipo = TipoZona.CASA,
        constelacion = Constelacion("Géminis", "Los Gemelos"),
        esNivel = true,
        esTutorial = false,
        etiquetas = listOf("Dualidad y reflejos", "Muros gemelos", "Simetría rota"),
        resumen = "Una Casa construida con simetría perfecta que está perdiendo su forma: ya no sabe cuál de sus dos caras es la verdadera.",
        tonoEmocional = null,
        ideaCentral = "Lo que construyes abajo cambia lo que puedes usar arriba.",
        exterior = "Dos torres gemelas suspendidas del Eje, unidas entre sí por puentes de latón.",
        ambientacion = "Cada pasillo se refleja a sí mismo.",
        narrativa = "Géminis es la casa de las dos caras de la verdad.",
        restauracion = "Al restaurar la Casa, la mitad oxidada se pule y los reflejos vuelven a ser exactos.",
        coleccionables = "3 o 4 poemas, en lugares que exigen dominar el anclaje reflejado.",
        paleta = "Dorados brillantes contra óxidos rojizos, con reflejos violeta.",
        iluminacion = null,
        sensacionVisual = null,
        tinte = "violeta",
        imagenUrl = null,
        salas = listOf(
            SalaResumen("atrio-de-los-gemelos", "El Atrio de los Gemelos", 1, esZonaSegura = false, esObservatorio = false),
            SalaResumen("galeria-de-espejos", "La Galería de Espejos", 2, esZonaSegura = false, esObservatorio = false),
            SalaResumen("sala-observatorio-dos-lentes", "El Observatorio de las Dos Lentes", 3, esZonaSegura = true, esObservatorio = true),
        ),
        mecanicas = listOf(
            MecanicaZona("anclaje-reflejado", "Anclaje reflejado", "Cada anclaje que el jugador coloca genera una copia invertida en el techo."),
        ),
    )

    val sala = RecorridoSala(
        sala = Sala(
            slug = "sala-observatorio-dos-lentes",
            nombre = "El Observatorio de las Dos Lentes",
            orden = 3,
            descripcion = "Observatorio mayor y zona segura.",
            aprendizaje = null,
            esZonaSegura = true,
            imagenUrl = null,
            zona = Referencia("geminis", "Casa de Géminis"),
            observatorio = Observatorio(
                slug = "observatorio-de-las-dos-lentes",
                nombre = "Observatorio de las Dos Lentes",
                ubicacion = "Casa de Géminis",
                resumen = "Donde el jugador encuentra las primeras huellas del Almanaque XI.",
                exterior = "Una cúpula doble suspendida sobre el puente más alto entre las dos torres.",
                interior = "Una sala circular dividida por una línea de latón en el suelo.",
                luz = null,
                audio = "Piano solo y lento; el tic-tac del Reloj se escucha amortiguado.",
                descubrimientos = null,
                contenidoBreve = "Primeras notas de XI: el descubrimiento.",
                imagenUrl = null,
            ),
            archivos = listOf(ArchivoEnSala("nota-xi", "Primera nota de XI", "notas-de-xi")),
            dialogos = listOf(
                Dialogo(
                    slug = "salida-observatorio-dos-lentes",
                    titulo = "Al salir del Observatorio de las Dos Lentes",
                    tipo = TipoDialogo.CITA,
                    momento = "Primera mención del Almanaque XI.",
                    personaje = Referencia("efemeride", "Efeméride"),
                    zona = Referencia("geminis", "Casa de Géminis"),
                    sala = Referencia("sala-observatorio-dos-lentes", "El Observatorio de las Dos Lentes"),
                    lineas = listOf(LineaDialogo(1, "El Almanaque XI presentó fallas. No es necesario que leas lo que dejó.", null, null)),
                ),
            ),
        ),
        anterior = SalaResumen("galeria-de-espejos", "La Galería de Espejos", 2, esZonaSegura = false, esObservatorio = false),
        siguiente = null,
        total = 3,
        tipoZona = TipoZona.CASA,
        tinteZona = "violeta",
    )

    val personajes = listOf(
        PersonajeResumen("almanaque-xii", "Almanaque XII", RolPersonaje.PROTAGONISTA, "Un autómata pequeño, curioso y silencioso, creado para reparar el Reloj y obedecer a Efeméride.", null),
        PersonajeResumen("efemeride", "Efeméride", RolPersonaje.GUIA, "La voz del Reloj: un holograma formado a partir de las memorias de sus creadores.", null),
        PersonajeResumen("almanaque-xi", "Almanaque XI", RolPersonaje.SECUNDARIO, "El almanaque anterior, relacionado con la ruptura del Gran Reloj.", null),
    )

    val personaje = Personaje(
        slug = "almanaque-xii",
        nombre = "Almanaque XII",
        titulo = "El duodécimo Almanaque",
        rol = RolPersonaje.PROTAGONISTA,
        estado = EstadoPersonaje.ACTIVO,
        resumen = "Un autómata pequeño, curioso y silencioso, creado para reparar el Reloj.",
        origen = "Es el duodécimo de su linaje. Despierta en la Sala de los Nichos, sin recuerdos.",
        motivacion = "Recolectar los fragmentos de estrella dispersos en cada Casa.",
        personalidad = null,
        arco = null,
        imagenUrl = null,
        rasgos = listOf(
            GrupoRasgos("apariencia", listOf(Rasgo("Capa", "Larga y oscura, cubierta de mapas estelares bordados en hilo dorado."))),
            GrupoRasgos("herramienta", listOf(Rasgo("El Astrolabio", "Un disco de latón con anillos concéntricos que giran."))),
        ),
        dialogos = emptyList(),
    )

    val mecanicas = Mecanicas(
        habilidades = listOf(
            Habilidad("salto", 1, "Salto", "Salto", "Altura variable", TipoHabilidad.MECANICA, "Salto base; altura variable según cuánto se mantiene el botón.", null, "Chispa breve y sonido de resorte al despegar.", mapOf("altura" to "variable"), null),
            Habilidad("punto-de-anclaje", 2, "Ancla", "Punto de anclaje", "6 u · 5 s", TipoHabilidad.MECANICA, "Fija una estrella temporal en una posición dentro de 6 unidades.", "El Astrolabio \"escribe\" una estrella diminuta en el vacío.", "Mira azul si es válido, roja si no.", mapOf("duracion_s" to "5", "max_activos" to "1", "alcance_unidades" to "6"), null),
            Habilidad("descansar", 8, "Descansar", "Descansar", null, TipoHabilidad.NARRATIVA, "En los observatorios se sienta y da cuerda a su llave.", null, null, emptyMap(), null),
        ),
        reglas = listOf(Regla("recarga", "Recarga", "Tocar suelo firme recarga los usos del gancho y la cuerda de escalada.")),
    )

    val categoriasArchivo = listOf(
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
        CategoriaArchivo("notas-de-xi", "Notas de XI", "Lo que el Almanaque XI dejó en los observatorios mayores.", null, emptyList()),
    )

    val archivo = Archivo(
        slug = "poema-gemelas",
        titulo = "Gemelas",
        categoria = "poemas",
        contenido = "Mi hermana y yo nacimos bajo la misma estrella y a las dos nos tocó la misma vida. Esta mañana ella eligió el mar y yo me quedé en tierra.",
        autor = "Anónimo (desde la Tierra)",
        ubicacion = "Los Puentes Paralelos",
        imagenUrl = null,
        zona = Referencia("geminis", "Casa de Géminis"),
        sala = Referencia("puentes-paralelos", "Los Puentes Paralelos"),
    )
}
