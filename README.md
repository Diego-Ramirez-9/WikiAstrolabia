# WikiAstrolabio

Aplicación móvil Android que funciona como wiki oficial del videojuego **Astrolabio**, un plataformero 2D de un jugador donde se asume el rol de **Almanaque XII** en su ascenso por el Gran Reloj Cósmico, usando puntos de anclaje y un gancho para superar abismos y obstáculos.

La app permite consultar la historia, el mundo (mapa, Casas y salas), los personajes, las mecánicas y los archivos del juego, con modo offline.

> **Estado actual:** base técnica lista. Mientras se termina la API, la app usa **respuestas falsas** con el contenido real del juego (ver [Modo API falsa](#modo-api-falsa)). El diseño visual es provisional y lo define el equipo de UI/UX.

## Stack técnico

| Área | Tecnología |
|---|---|
| Lenguaje | Kotlin 2.4 |
| Interfaz | Jetpack Compose + Material 3 |
| Navegación | Navigation Compose con rutas tipadas (`@Serializable`) |
| Arquitectura | MVVM por capas (UI, dominio, datos) + Repository + casos de uso |
| Inyección de dependencias | Hilt |
| Red | Retrofit 3 + OkHttp 5 + kotlinx.serialization |
| Caché offline | Room (respuestas de la API) + DataStore (versión del contenido) |
| Imágenes | Coil 3 (con `FilterQuality.None` para pixel art) |
| Backend | API REST propia en **Node.js + Express**, desplegada en **Vercel** |
| SDK mínimo | Android 8.0 (API 26) |

**La app solo habla con nuestra API.** Supabase se usa únicamente como base de datos (PostgreSQL) y almacenamiento de imágenes, y solo la API tiene sus credenciales. La app no usa `supabase-kt` ni guarda claves de Supabase.

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
3. (Opcional) Configura `local.properties` en la raíz del proyecto. Este archivo **no se sube a GitHub** y Android Studio ya lo crea con `sdk.dir`:
   ```properties
   # true (por defecto): usa las respuestas falsas de app/src/debug/assets/fake-api
   # false: llama a la API real
   USE_FAKE_API=true

   # URL de la API para el build debug (por defecto, la de producción en gradle.properties).
   # Para la API corriendo en tu computadora, desde el emulador:
   # API_BASE_URL=http://10.0.2.2:3000/api/v1/
   ```
4. Espera a que Gradle sincronice y ejecuta la app en un emulador o dispositivo. Si la sincronización falla con un error `PKIX path building failed`, revisa [Solución de problemas](#solución-de-problemas).

La URL de producción está en `gradle.properties` (`astrolabio.apiUrl`). **Pendiente:** reemplazarla por el dominio real cuando se despliegue la API en Vercel.

## Modo API falsa

Mientras no exista la API, el build debug responde cada petición con un JSON de `app/src/debug/assets/fake-api/`, generado a partir de `database/02_seed.sql` del repositorio de la API. Funciona a nivel HTTP (`FakeApiInterceptor`), así que Retrofit, los DTO y la caché se prueban igual que con la API real. Pasar a la API real es solo poner `USE_FAKE_API=false`.

| Petición | Archivo |
|---|---|
| `GET /personajes` | `fake-api/personajes.json` |
| `GET /personajes/almanaque-xii` | `fake-api/personajes/almanaque-xii.json` |
| `GET /zonas?tipo=casa` | `fake-api/zonas.tipo-casa.json` |

Si cambia el seed o la forma de algún JSON, vuelve a generar los archivos y corre las pruebas:

```bash
python tools/fake-api/generar_fake_api.py ../astrolabio-api/database/02_seed.sql
```

```bash
./gradlew testDebugUnitTest
```

La prueba `FakeApiAssetsTest` falla si algún JSON ya no coincide con los DTO de la app.

## Estructura del proyecto

```
app/src/main/java/com/equipo/wikiastrolabio/
├── WikiAstrolabioApp.kt      Application (Hilt + configuración de Coil)
├── MainActivity.kt           Splash del sistema + tema + AstrolabioApp
├── MainViewModel.kt          Consulta /contenido/version mientras se ve el splash
├── di/                       Módulos de Hilt (red, base de datos, repositorios)
├── domain/                   Lógica pura de Kotlin, sin Android
│   ├── model/                Modelos que usa la UI (Personaje, Zona, Resultado…)
│   ├── repository/           Interfaces de los repositorios
│   └── usecase/<sección>/    Un caso de uso por operación (ObtenerPersonajesUseCase…)
├── data/                     De dónde salen los datos
│   ├── remote/               AstrolabioApi (Retrofit), DTO y FakeApiInterceptor
│   ├── local/                Room (caché de respuestas) y DataStore
│   ├── mapper/               DTO → modelo de dominio
│   └── repository/           Implementaciones + CacheDeContenido (estrategia offline)
└── ui/
    ├── AstrolabioApp.kt      Scaffold con la barra de navegación inferior
    ├── navigation/           Rutas, pestañas y NavHost
    ├── theme/                Colores, tipografía y espaciados
    ├── components/           Componentes compartidos (estados, tarjetas, imágenes…)
    ├── common/               UiState y ContenidoViewModel
    ├── preview/              Datos de ejemplo para las @Preview
    └── feature/<pantalla>/   Una carpeta por pantalla: XScreen.kt, XViewModel.kt, components/
```

Pantallas actuales: Inicio, Historia (+ entrada), Mundo (mapa interactivo), Zonas (con pestañas de Casas), Salas, Personajes (+ ficha), Mecánicas y Archivos (+ detalle). Novedades queda fuera por ahora.

### Guía para UI/UX

Cada pantalla tiene dos funciones en `XScreen.kt`:

- **`XRoute`**: conecta el ViewModel. Normalmente no hay que tocarla.
- **`XScreen`**: solo dibuja el estado que recibe. **Aquí va el diseño.** Tiene su `@Preview` con datos de ejemplo, así que se puede diseñar sin emulador ni API.

| Quiero cambiar… | Archivo |
|---|---|
| Colores | `ui/theme/Color.kt` y `Theme.kt` (no usar colores sueltos en las pantallas) |
| Fuentes pixel | `.ttf` en `app/src/main/res/font/` + `ui/theme/Type.kt` |
| Espaciados | `ui/theme/Espaciado.kt` |
| Carga, error y "sin conexión" de toda la app | `ui/components/ContenidoSegunEstado.kt` |
| Barra superior / barra inferior | `ui/components/BarraSuperior.kt` / `ui/AstrolabioApp.kt` |
| Pestañas de la barra inferior | `ui/navigation/DestinoPrincipal.kt` |
| Splash | `res/values/themes.xml` (`Theme.WikiAstrolabio.Splash`) |
| Íconos y sprites | `res/drawable/` con prefijo: `ic_` íconos, `spr_` sprites, `bg_` fondos, `img_` ilustraciones |
| Marcador de "recurso pendiente" | `res/drawable/img_recurso_pendiente.xml` |

Los íconos actuales (`ic_nav_*`, `ic_atras`) y la paleta son **provisionales**.

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

El build **release** usa R8 (minificación y reducción de recursos) y siempre apunta a la API real.

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

Se usa versionado semántico `MAJOR.MINOR.PATCH`. La versión actual es `0.1.0` (base técnica, aún sin entregar).

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
