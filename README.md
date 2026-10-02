# Almanaque Wiki

Aplicación móvil Android que funciona como wiki oficial del videojuego **Almanaque XII**, un plataformero 2D de un jugador donde se asume el rol de Almanaque XII en su ascenso por el Gran Reloj Cósmico, usando puntos de anclaje y un gancho para superar abismos y obstáculos.

La app permite consultar personajes, historia, niveles y controles del juego, con modo offline.



## Stack técnico

- **Lenguaje:** Kotlin
- **Interfaz:** Jetpack Compose
- **Arquitectura:** MVVM con capas (UI, dominio, datos) y patrón Repository
- **Inyección de dependencias:** Hilt
- **Caché local:** Room
- **Backend:** Supabase (API REST, PostgreSQL y Storage) mediante `supabase-kt`
- **SDK mínimo:** Android 8.0 (API 26)
- **IDE:** Android Studio

## Primeros pasos

1. Clona el repositorio desde Android Studio: **File → New → Project from Version Control**, o con:
   ```bash
   git clone https://github.com/<usuario>/<repositorio>.git
   ```
2. Cambia a la rama `develop`:
   ```bash
   git checkout develop
   ```
3. Crea el archivo `local.properties` en la raíz del proyecto (si no existe) y agrega las claves de Supabase que te comparta el equipo:
   ```properties
   SUPABASE_URL=https://xxxxxxxx.supabase.co
   SUPABASE_ANON_KEY=tu_clave_publica
   ```
   Este archivo **no se sube a GitHub**.
4. Espera a que Gradle sincronice y ejecuta la app en un emulador o dispositivo.

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
9. Nunca subir `local.properties`, claves de Supabase ni archivos `.apk` o `.aab`.

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
| `chore:` | Configuración y tareas de mantenimiento |

## Versiones

Se usa versionado semántico `MAJOR.MINOR.PATCH`.

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


