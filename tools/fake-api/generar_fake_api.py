"""
Genera las respuestas falsas de la API (modo fake) a partir del seed de la base de datos.

La app, en debug y con USE_FAKE_API=true, no llama a la red: FakeApiInterceptor lee estos
JSON desde app/src/debug/assets/fake-api/. Así se prueba todo el flujo real (Retrofit,
kotlinx.serialization, caché en Room) con el contenido verdadero del juego.

Uso (desde la raíz del repositorio):
    python tools/fake-api/generar_fake_api.py <ruta/al/02_seed.sql>

Cada respuesta imita docs/ENDPOINTS.md del repositorio de la API:
    GET /personajes/almanaque-xii  ->  fake-api/personajes/almanaque-xii.json
    GET /zonas?tipo=casa           ->  fake-api/zonas.tipo-casa.json

Si el backend cambia la forma de algún endpoint, actualiza la función correspondiente aquí,
vuelve a generar y corre los tests (FakeApiAssetsTest valida que los JSON sigan
deserializándose con los DTO).
"""

import json
import re
import shutil
import sys
from dataclasses import dataclass
from pathlib import Path

RAIZ_REPO = Path(__file__).resolve().parents[2]
SALIDA = RAIZ_REPO / "app" / "src" / "debug" / "assets" / "fake-api"

# Fecha fija para GET /contenido/version (la app la compara contra la que tiene guardada).
VERSION_CONTENIDO = "2026-10-18T15:01:22.946Z"

ORDEN_CATEGORIAS_RASGO = ["apariencia", "expresion", "herramienta", "comunicacion", "presencia"]


# ---------------------------------------------------------------------------
# Lectura del SQL (solo lo que usa 02_seed.sql: insert ... values y update ... set)
# ---------------------------------------------------------------------------

@dataclass(frozen=True)
class Referencia:
    """Subconsulta `(select id from <tabla> where slug = '<slug>')`."""
    tabla: str
    slug: str


TOKEN = re.compile(
    r"""
    (?P<espacio>\s+)
  | (?P<comentario>--[^\n]*)
  | (?P<texto>'(?:[^']|'')*')
  | (?P<numero>-?\d+(?:\.\d+)?)
  | (?P<palabra>[A-Za-z_][A-Za-z_0-9]*)
  | (?P<simbolo>[(),;\[\]=])
    """,
    re.VERBOSE,
)


def tokenizar(sql: str):
    tokens = []
    pos = 0
    while pos < len(sql):
        m = TOKEN.match(sql, pos)
        if not m:
            raise ValueError(f"Carácter inesperado en la posición {pos}: {sql[pos:pos + 30]!r}")
        pos = m.end()
        tipo = m.lastgroup
        if tipo in ("espacio", "comentario"):
            continue
        valor = m.group(tipo)
        if tipo == "texto":
            valor = valor[1:-1].replace("''", "'")
        elif tipo == "palabra":
            valor = valor.lower()
        tokens.append((tipo, valor))
    return tokens


class Lector:
    def __init__(self, tokens):
        self.tokens = tokens
        self.i = 0

    def ver(self):
        return self.tokens[self.i] if self.i < len(self.tokens) else (None, None)

    def tomar(self, esperado=None):
        tipo, valor = self.ver()
        if esperado is not None and valor != esperado:
            raise ValueError(f"Se esperaba {esperado!r} y llegó {valor!r} (token {self.i})")
        self.i += 1
        return valor

    def valor(self):
        tipo, valor = self.ver()
        if tipo == "texto":
            self.i += 1
            return valor
        if tipo == "numero":
            self.i += 1
            return float(valor) if "." in valor else int(valor)
        if valor in ("null", "true", "false"):
            self.i += 1
            return {"null": None, "true": True, "false": False}[valor]
        if valor == "array":
            self.tomar("array")
            self.tomar("[")
            elementos = []
            while self.ver()[1] != "]":
                elementos.append(self.valor())
                if self.ver()[1] == ",":
                    self.tomar(",")
            self.tomar("]")
            return elementos
        if valor == "(":
            self.tomar("(")
            self.tomar("select")
            self.tomar("id")
            self.tomar("from")
            tabla = self.tomar()
            self.tomar("where")
            self.tomar("slug")
            self.tomar("=")
            slug = self.tomar()
            self.tomar(")")
            return Referencia(tabla, slug)
        raise ValueError(f"Valor no soportado: {valor!r} (token {self.i})")


def leer_seed(ruta: Path) -> dict:
    lector = Lector(tokenizar(ruta.read_text(encoding="utf-8")))
    tablas: dict[str, list[dict]] = {}

    while lector.ver()[0] is not None:
        palabra = lector.tomar()
        if palabra in ("begin", "commit", ";"):
            continue
        if palabra == "insert":
            lector.tomar("into")
            tabla = lector.tomar()
            lector.tomar("(")
            columnas = []
            while lector.ver()[1] != ")":
                columnas.append(lector.tomar())
                if lector.ver()[1] == ",":
                    lector.tomar(",")
            lector.tomar(")")
            lector.tomar("values")
            filas = tablas.setdefault(tabla, [])
            while True:
                lector.tomar("(")
                valores = []
                while lector.ver()[1] != ")":
                    valores.append(lector.valor())
                    if lector.ver()[1] == ",":
                        lector.tomar(",")
                lector.tomar(")")
                fila = dict(zip(columnas, valores))
                fila["id"] = len(filas) + 1
                filas.append(fila)
                if lector.ver()[1] == ",":
                    lector.tomar(",")
                    continue
                lector.tomar(";")
                break
        elif palabra == "update":
            tabla = lector.tomar()
            lector.tomar("set")
            cambios = {}
            while True:
                columna = lector.tomar()
                lector.tomar("=")
                cambios[columna] = lector.valor()
                if lector.ver()[1] == ",":
                    lector.tomar(",")
                    continue
                break
            lector.tomar("where")
            lector.tomar("slug")
            lector.tomar("=")
            slug = lector.tomar()
            lector.tomar(";")
            for fila in tablas[tabla]:
                if fila["slug"] == slug:
                    fila.update(cambios)
        else:
            raise ValueError(f"Sentencia no soportada: {palabra!r}")

    # Resuelve las subconsultas a ids.
    for filas in tablas.values():
        for fila in filas:
            for columna, valor in fila.items():
                if isinstance(valor, Referencia):
                    destino = next(f for f in tablas[valor.tabla] if f["slug"] == valor.slug)
                    fila[columna] = destino["id"]
    return tablas


# ---------------------------------------------------------------------------
# Construcción de las respuestas (forma de docs/ENDPOINTS.md)
# ---------------------------------------------------------------------------

def publicado(fila) -> bool:
    return fila.get("publicado", True)


def ordenadas(filas):
    return sorted(filas, key=lambda f: (f.get("orden", 0), f["id"]))


def por_id(filas, id_):
    return next((f for f in filas if f["id"] == id_), None)


def referencia(fila):
    return None if fila is None else {"slug": fila["slug"], "nombre": fila["nombre"]}


def construir(t: dict) -> dict[str, object]:
    r: dict[str, object] = {}

    r["salud"] = {"estado": "ok", "baseDeDatos": "ok"}
    r["contenido/version"] = {"actualizadoEn": VERSION_CONTENIDO}

    # Historia
    secciones = ordenadas(t["secciones_historia"])
    lore = [l for l in ordenadas(t["lore"]) if publicado(l)]
    r["historia"] = [
        {
            "slug": s["slug"],
            "titulo": s["titulo"],
            "entradas": [
                {"slug": l["slug"], "titulo": l["titulo"], "contenido": l["contenido"],
                 "imagenUrl": l.get("imagen_url")}
                for l in lore if l["seccion_id"] == s["id"]
            ],
        }
        for s in secciones
    ]
    for l in lore:
        seccion = por_id(secciones, l["seccion_id"])
        r[f"historia/{l['slug']}"] = {
            "slug": l["slug"], "titulo": l["titulo"], "contenido": l["contenido"],
            "imagenUrl": l.get("imagen_url"),
            "seccion": {"slug": seccion["slug"], "titulo": seccion["titulo"]},
        }

    # Mapa y zonas
    zonas = [z for z in ordenadas(t["zonas"]) if publicado(z)]
    mapa = t["mapas"][0]
    r["mapa"] = {
        "titulo": mapa["titulo"],
        "descripcion": mapa.get("descripcion"),
        "imagenUrl": mapa.get("imagen_url"),
        "puntos": [
            {"slug": z["slug"], "nombre": z["nombre"], "subtitulo": z.get("subtitulo"), "tipo": z["tipo"],
             "resumen": z.get("resumen"), "x": float(z["mapa_x"]), "y": float(z["mapa_y"])}
            for z in zonas if z.get("mapa_x") is not None
        ],
    }

    def zona_resumen(z):
        return {"slug": z["slug"], "nombre": z["nombre"], "subtitulo": z.get("subtitulo"), "tipo": z["tipo"],
                "etiquetas": z.get("etiquetas", []), "imagenUrl": z.get("imagen_url")}

    r["zonas"] = [zona_resumen(z) for z in zonas]
    for tipo in sorted({z["tipo"] for z in zonas}):
        r[f"zonas?tipo={tipo}"] = [zona_resumen(z) for z in zonas if z["tipo"] == tipo]

    salas = ordenadas(t["salas"])
    observatorios = t["observatorios"]
    salas_con_observatorio = {o["sala_id"] for o in observatorios}
    for z in zonas:
        r[f"zonas/{z['slug']}"] = {
            "slug": z["slug"], "nombre": z["nombre"], "subtitulo": z.get("subtitulo"), "tipo": z["tipo"],
            "constelacion": None if not z.get("constelacion") else {
                "nombre": z["constelacion"], "sobrenombre": z.get("constelacion_sobrenombre")},
            "etiquetas": z.get("etiquetas", []),
            "resumen": z.get("resumen"), "exterior": z.get("exterior"), "ambientacion": z.get("ambientacion"),
            "narrativa": z.get("narrativa"), "restauracion": z.get("restauracion"),
            "imagenUrl": z.get("imagen_url"),
            "salas": [
                {"slug": s["slug"], "nombre": s["nombre"], "orden": s.get("orden", 0),
                 "esZonaSegura": s.get("es_zona_segura", False), "esObservatorio": s["id"] in salas_con_observatorio}
                for s in salas if s["zona_id"] == z["id"]
            ],
            "mecanicas": [
                {"slug": m["slug"], "nombre": m["nombre"], "descripcion": m["descripcion"]}
                for m in ordenadas(t["mecanicas_zona"]) if m["zona_id"] == z["id"]
            ],
        }

    # Salas
    archivos = [a for a in ordenadas(t["archivos"]) if publicado(a)]
    categorias = ordenadas(t["categorias_archivo"])
    dialogos = ordenadas(t["dialogos"])
    lineas = sorted(t["dialogo_lineas"], key=lambda l: (l["dialogo_id"], l["orden"]))

    def lineas_de(dialogo, completas):
        resultado = []
        for l in lineas:
            if l["dialogo_id"] != dialogo["id"]:
                continue
            linea = {"orden": l["orden"], "texto": l["texto"]}
            if completas:
                linea.update({"indicacion": l.get("indicacion"), "lugar": l.get("lugar")})
            resultado.append(linea)
        return resultado

    for s in salas:
        obs = next((o for o in observatorios if o["sala_id"] == s["id"]), None)
        r[f"salas/{s['slug']}"] = {
            "slug": s["slug"], "nombre": s["nombre"], "descripcion": s.get("descripcion"),
            "aprendizaje": s.get("aprendizaje"), "esZonaSegura": s.get("es_zona_segura", False),
            "imagenUrl": s.get("imagen_url"),
            "zona": referencia(por_id(t["zonas"], s["zona_id"])),
            "observatorio": None if obs is None else {
                "nombre": obs["nombre"], "exterior": obs.get("exterior"), "interior": obs.get("interior"),
                "luz": obs.get("luz"), "descubrimientos": obs.get("descubrimientos")},
            "archivos": [
                {"slug": a["slug"], "titulo": a["titulo"], "categoria": por_id(categorias, a["categoria_id"])["slug"]}
                for a in archivos if a.get("sala_id") == s["id"]
            ],
            "dialogos": [
                {"slug": d["slug"], "titulo": d["titulo"], "lineas": lineas_de(d, completas=False)}
                for d in dialogos if d.get("sala_id") == s["id"]
            ],
        }

    # Personajes
    personajes = [p for p in ordenadas(t["personajes"]) if publicado(p)]
    r["personajes"] = [
        {"slug": p["slug"], "nombre": p["nombre"], "rol": p["rol"], "resumen": p.get("resumen"),
         "imagenUrl": p.get("imagen_url")}
        for p in personajes
    ]
    for p in personajes:
        rasgos = [x for x in ordenadas(t["personaje_rasgos"]) if x["personaje_id"] == p["id"]]
        r[f"personajes/{p['slug']}"] = {
            "slug": p["slug"], "nombre": p["nombre"], "titulo": p.get("titulo"), "rol": p["rol"],
            "estado": p.get("estado", "activo"), "resumen": p.get("resumen"),
            "origen": p.get("origen"), "motivacion": p.get("motivacion"),
            "personalidad": p.get("personalidad"), "arco": p.get("arco"),
            "imagenUrl": p.get("imagen_url"),
            "rasgos": {
                categoria: [{"nombre": x["nombre"], "descripcion": x["descripcion"]}
                            for x in rasgos if x["categoria"] == categoria]
                for categoria in ORDEN_CATEGORIAS_RASGO
                if any(x["categoria"] == categoria for x in rasgos)
            },
            "dialogos": [
                {"slug": d["slug"], "titulo": d["titulo"], "tipo": d["tipo"], "momento": d.get("momento"),
                 "lineas": lineas_de(d, completas=True)}
                for d in dialogos if d["personaje_id"] == p["id"]
            ],
        }

    # Mecánicas
    r["mecanicas"] = {
        "habilidades": [
            {"slug": h["slug"], "numero": h.get("orden", 0), "nombreCorto": h["nombre_corto"],
             "nombre": h["nombre"], "etiqueta": h.get("etiqueta"), "tipo": h["tipo"],
             "descripcion": h["descripcion"], "origenMundo": h.get("origen_mundo"),
             "senalJugador": h.get("senal_jugador"), "parametros": json.loads(h.get("parametros") or "{}"),
             "iconoUrl": h.get("icono_url")}
            for h in ordenadas(t["habilidades"])
        ],
        "reglas": [
            {"slug": x["slug"], "nombre": x["nombre"], "descripcion": x["descripcion"]}
            for x in ordenadas(t["reglas"])
        ],
    }

    # Archivos
    r["archivos"] = [
        {"slug": c["slug"], "nombre": c["nombre"], "descripcion": c.get("descripcion"),
         "iconoUrl": c.get("icono_url"),
         "elementos": [
             {"slug": a["slug"], "titulo": a["titulo"], "ubicacion": a.get("ubicacion"),
              "imagenUrl": a.get("imagen_url")}
             for a in archivos if a["categoria_id"] == c["id"]
         ]}
        for c in categorias
    ]
    for a in archivos:
        r[f"archivos/{a['slug']}"] = {
            "slug": a["slug"], "titulo": a["titulo"],
            "categoria": por_id(categorias, a["categoria_id"])["slug"],
            "contenido": a["contenido"], "autor": a.get("autor"), "ubicacion": a.get("ubicacion"),
            "imagenUrl": a.get("imagen_url"),
            "zona": referencia(por_id(t["zonas"], a.get("zona_id"))),
            "sala": referencia(por_id(salas, a.get("sala_id"))),
        }

    return r


def ruta_archivo(endpoint: str) -> Path:
    """`zonas?tipo=casa` -> zonas.tipo-casa.json (misma regla que FakeApiInterceptor)."""
    ruta, _, consulta = endpoint.partition("?")
    if consulta:
        parametros = sorted(consulta.split("&"))
        ruta += "." + ".".join(p.replace("=", "-") for p in parametros)
    return SALIDA / f"{ruta}.json"


def main():
    if len(sys.argv) != 2:
        print(__doc__)
        sys.exit(1)
    respuestas = construir(leer_seed(Path(sys.argv[1])))
    if SALIDA.exists():
        shutil.rmtree(SALIDA)
    for endpoint, datos in respuestas.items():
        destino = ruta_archivo(endpoint)
        destino.parent.mkdir(parents=True, exist_ok=True)
        destino.write_text(json.dumps({"data": datos}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(f"{len(respuestas)} respuestas generadas en {SALIDA.relative_to(RAIZ_REPO)}")


if __name__ == "__main__":
    main()
