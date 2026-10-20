# WikiAstrolabio

Aplicación móvil Android que funciona como wiki oficial del videojuego **Astrolabio**, un plataformero 2D de un jugador donde se asume el rol de **Almanaque XII** en su ascenso por el Gran Reloj Cósmico, usando puntos de anclaje y un gancho para superar abismos y obstáculos.

La app muestra la historia, el mundo (mapa, Casas y salas), los personajes, las mecánicas y los archivos del juego. **Todo el contenido viene de la API del equipo** y se guarda para consultarlo sin conexión.

## Stack técnico

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.4 |
| Interfaz | Jetpack Compose + Material 3, con sistema de diseño propio en pixel art |
| Navegación | Navigation Compose con rutas tipadas (`@Serializable`) |
| Arquitectura | MVVM por capas (UI, dominio, datos) + Repository + casos de uso |
| Inyección de dependencias | Hilt |
| Red | Retrofit 3 + OkHttp 5 + kotlinx.serialization |
| Caché offline | Room (respuestas de la API) + DataStore (versión del contenido) |
| Imágenes | Coil 3 (con `FilterQuality.None` para pixel art) |
| Backend | API REST propia en **Node.js + Express**, en **Vercel**: `https://astrolabio-api.vercel.app/api/v1/` |
| SDK mínimo | Android 8.0 (API 26) |

**La app solo habla con nuestra API.** Supabase se usa únicamente como base de datos (PostgreSQL) y almacenamiento de imágenes, y solo la API tiene sus credenciales. La app no usa `supabase-kt` ni guarda claves.

```
App (Compose → ViewModel → Caso de uso → Repository) ──HTTPS/JSON──▶ API Node.js (Vercel) ──▶ Supabase
                                            └──▶ Room (caché offline)
```

## Primeros pasos

1. Clona el repositorio desde Android Studio: **File → New → Project from Version Control**, o con:
   ```bash
   git clone https://github.com/<usuario>/<repositorio>.git
   ```
2. Cambia a la rama `develop`:
   ```bash
   git checkout develop
   ```
3. Espera a que Gradle sincronice y ejecuta la app en un emulador o dispositivo con internet. Si la sincronización falla con `PKIX path building failed`, revisa [Solución de problemas](#solución-de-problemas).

La URL de la API está en `gradle.properties` (`astrolabio.apiUrl`). Para probar el build debug contra otro despliegue (por ejemplo, una vista previa de Vercel), agrégala a `local.properties`, que no se sube a GitHub:

```properties
API_BASE_URL=https://otro-despliegue.vercel.app/api/v1/
```

## Cómo usa la app la API

- Al abrir, consulta `GET /contenido/version` en segundo plano. Si la fecha cambió, cada pantalla vuelve a descargar su contenido la próxima vez que se abre; si no, usa lo guardado en Room sin gastar datos.
- Sin internet se muestra lo último guardado. Una sección que nunca se abrió muestra "Sin conexión" con botón de reintentar.
- Deslizar hacia abajo en cualquier pantalla fuerza la descarga.
- Se muestran **todos** los campos que manda la API: en una Casa, su constelación, tono emocional, idea central, coleccionables, paleta, iluminación y restauración; en un observatorio, su contenido, sonido y descubrimientos; en los diálogos, quién habla y en qué sala ocurren.
- **Imágenes:** mientras `imagenUrl` / `iconoUrl` vengan en `null`, cada ilustración muestra un cielo estrellado propio de ese elemento con la leyenda "Ilustración pendiente". En cuanto el equipo suba la imagen a Supabase Storage y ponga su URL en la base de datos, la app la muestra sola, sin publicar otra versión.

### Pruebas de contrato con la API

`app/src/test/resources/api-v1/` guarda las 60 respuestas reales de la API y `ContratoApiTest` comprueba que la app las sigue entendiendo. Cuando el equipo de la API cambie algo, actualízalas y corre las pruebas:

```bash
python tools/api/actualizar_respuestas_prueba.py
```

```bash
./gradlew testDebugUnitTest
```

## Estructura del proyecto

```
app/src/main/java/com/equipo/wikiastrolabio/
├── WikiAstrolabioApp.kt      Application (Hilt + configuración de Coil)
├── MainActivity.kt           Splash del sistema + tema + AstrolabioApp
├── MainViewModel.kt          Consulta /contenido/version al abrir la app
├── di/                       Módulos de Hilt (red, base de datos, repositorios)
├── domain/                   Lógica pura de Kotlin, sin Android
│   ├── model/                Modelos que usa la UI (Personaje, Zona, RecorridoSala…)
│   ├── repository/           Interfaces de los repositorios
│   └── usecase/<sección>/    Un caso de uso por operación
├── data/                     De dónde salen los datos
│   ├── remote/               AstrolabioApi (Retrofit) y DTO (el JSON tal como llega)
│   ├── local/                Room (caché de respuestas) y DataStore
│   ├── mapper/               DTO → modelo de dominio
│   └── repository/           Implementaciones + CacheDeContenido (estrategia offline)
└── ui/
    ├── AstrolabioApp.kt      Scaffold con la barra de navegación inferior
    ├── navigation/           Rutas, pestañas y NavHost
    ├── theme/                Tokens de diseño: color, tipografía, formas y espaciado
    ├── components/           Componentes del sistema de diseño (paneles, encabezados, filas…)
    │   └── pixel/            Íconos y decoraciones en pixel art dibujados por código
    ├── common/               UiState, ContenidoViewModel y colores por zona
    ├── preview/              Datos de ejemplo SOLO para las @Preview
    └── feature/<pantalla>/   XScreen.kt (dibuja), XViewModel.kt (estado), components/
```

Cada ViewModel vive junto a su pantalla en `ui/feature/<pantalla>/` (organización por funcionalidad).

## Sistema de diseño

La identidad visual parte del Figma del equipo de UI/UX (azul nocturno, cobre y ámbar, Pixelify Sans) y está ordenada como sistema para que todas las pantallas se vean consistentes:

| Pieza | Dónde | Regla |
|---|---|---|
| Color | `ui/theme/Color.kt`, `Theme.kt` | 60 % azul nocturno, 30 % pergamino, 10 % acento. El ámbar es el único color de interacción. |
| Color por zona | `LocalAcento`, `ui/common/ColoresZona.kt` | Cada Casa tiñe su pantalla con su `tinteFondo` de la API (Géminis violeta, Acuario turquesa); el Eje es latón y el desenlace, cobre. |
| Tipografía | `ui/theme/Type.kt` | Pixelify Sans para títulos, etiquetas y números; serif para los textos de lectura. Escala 1.25: 11 · 12 · 13 · 15 · 19 · 24 · 30 · 37. |
| Formas | `Theme.kt` | Esquinas cortadas (como un escalón de píxel) en lugar de redondeadas. |
| Espaciado | `ui/theme/Espaciado.kt` | Base de 4 dp, margen de pantalla de 20 dp, áreas tocables de 48 dp. |
| Componentes | `ui/components/` | `EncabezadoPantalla`, `PanelPixel`, `EtiquetaSeccion`, `FilaIndice`, `Insignia`, `ChipEnlace`, `FichaDatos`, `CitaDestacada`, `TextoLectura`, `MarcoIlustracion`, `BloqueDialogo`, `NavegacionVecinos`, `BotonPixel` y los estados de carga y error. |
| Íconos | `ui/components/pixel/IconosPixel.kt` | Cuadrícula de 16×16; los nuevos se dibujan con un patrón de texto (`IconoPatron`). |

Para cambiar el aspecto de toda la app basta con editar los tokens del tema o los componentes: las pantallas solo los combinan. Cada pantalla y componente tiene su `@Preview` con datos de ejemplo.

## Pruebas y compilación

```bash
./gradlew testDebugUnitTest
```

```bash
./gradlew assembleDebug
```

```bash
./gradlew lintDebug
```

El build **release** usa R8 (minificación y reducción de recursos; APK de unos 2 MB) y siempre apunta a la API de producción. Falta configurar su firma (keystore fuera del repositorio).

## Solución de problemas

**`PKIX path building failed` / `unable to find valid certification path` al sincronizar Gradle.** Algunos antivirus (por ejemplo, AVG con *Web Shield*) inspeccionan HTTPS con su propio certificado, y el Java de Android Studio no confía en él. Opciones:

1. En el antivirus, desactivar el escaneo HTTPS o agregar una excepción para Android Studio y `java.exe`.
2. O crear un almacén de certificados que incluya el del antivirus (ejemplo para AVG, en Git Bash):
   ```bash
   cp "/c/Program Files/Android/Android Studio/jbr/lib/security/cacerts" ~/.gradle/cacerts-antivirus
   ```
   ```bash
   "/c/Program Files/Android/Android Studio/jbr/bin/keytool" -importcert -noprompt -alias antivirus -file "/c/ProgramData/AVG/Antivirus/wscert.pem" -keystore ~/.gradle/cacerts-antivirus -storepass changeit
   ```
   y agregar a `~/.gradle/gradle.properties` (el de tu usuario, **no** el del proyecto):
   ```properties
   systemProp.javax.net.ssl.trustStore=C:/Users/<tu-usuario>/.gradle/cacerts-antivirus
   systemProp.javax.net.ssl.trustStorePassword=changeit
   ```

## Estrategia de ramas (Git Flow)

| Rama | Sale de | Se une a | Uso |
|---|---|---|---|
| `main` | — | — | Solo versiones entregadas, con etiqueta (`v1.0.0`). |
| `develop` | `main` | — | Integración del trabajo del equipo. |
| `feature/<nombre>` | `develop` | `develop` | Una por tarea. Ej.: `feature/pantalla-personajes`. |
| `release/x.y.z` | `develop` | `main` y `develop` | Pruebas y arreglos finales antes de entregar. |
| `hotfix/x.y.z` | `main` | `main` y `develop` | Corrección urgente de algo ya entregado. |

## Acuerdo del equipo

Las ramas no tienen protección en GitHub, así que estas reglas dependen de que todos las cumplamos.

1. Antes de empezar una tarea, avisa al equipo qué rama vas a trabajar, para no duplicar trabajo.
2. **Nadie hace push directo a `main` ni a `develop`.** Antes de programar, confirma que estás en tu rama `feature/*`.
3. Toda feature entra a `develop` mediante **Pull Request**, revisado por **al menos otro integrante**. Nadie aprueba su propio PR.
4. Haz `git pull` de `develop` antes de crear tu rama y antes de abrir el PR.
5. Commits pequeños y con prefijo (ver abajo).
6. Borra tu rama `feature/*` después del merge.
7. Las ramas `release/*` y `hotfix/*` las maneja solo el encargado de versiones del sprint.
8. **Prohibido `git push --force`** sobre `main` y `develop`.
9. Nunca subir `local.properties`, credenciales, ni archivos `.apk` o `.aab`.

## Flujo de trabajo diario

```bash
# 1. Actualizar develop y crear tu rama
git checkout develop
git pull
git checkout -b feature/nombre-de-la-tarea

# 2. Programar y hacer commits pequeños
git add .
git commit -m "feat: descripción corta de lo que hiciste"

# 3. Subir la rama
git push -u origin feature/nombre-de-la-tarea
```

Después, abre un **Pull Request** hacia `develop` en GitHub, pide la revisión de otro integrante y, tras la aprobación, haz el merge y borra la rama.

### Convención de commits

| Prefijo | Uso |
|---|---|
| `feat:` | Funcionalidad nueva |
| `fix:` | Corrección de un error |
| `docs:` | Documentación |
| `style:` | Formato o estilos visuales |
| `refactor:` | Reorganizar código sin cambiar lo que hace |
| `test:` | Pruebas |
| `chore:` | Configuración y tareas de mantenimiento |

## Versiones

Se usa versionado semántico `MAJOR.MINOR.PATCH`. La versión actual es `0.1.0` (aún sin entregar).

**Release (al terminar un sprint)**
1. Crear `release/x.y.z` desde `develop`.
2. Subir `versionName` y `versionCode` en `app/build.gradle.kts` y arreglar solo detalles finales.
3. Unir la rama a `main` y crear la etiqueta `vx.y.z`.
4. Unir también la rama a `develop`, para no perder los arreglos.
5. Borrar la rama `release`.

**Hotfix**
1. Crear `hotfix/x.y.z` desde `main`, corregir y subir la versión de parche.
2. Unir a `main` y etiquetar.
3. Unir también a `develop`.
