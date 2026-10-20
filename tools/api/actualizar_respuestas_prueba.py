"""
Descarga todas las respuestas de la API de Astrolabio y las guarda como datos de prueba en
app/src/test/resources/api-v1/. ContratoApiTest las lee para comprobar que los DTO y los
mappers de la app siguen entendiendo lo que manda la API.

Úsalo cuando el equipo de la API cambie un endpoint o agregue contenido:
    python tools/api/actualizar_respuestas_prueba.py [url_base]

url_base por defecto: https://astrolabio-api.vercel.app/api/v1/
Recorre los listados y sigue cada enlace a su detalle (historia, zonas, salas, personajes,
archivos). Usa curl para respetar los certificados del sistema.
"""

import json
import pathlib
import shutil
import subprocess
import sys

RAIZ_REPO = pathlib.Path(__file__).resolve().parents[2]
SALIDA = RAIZ_REPO / "app" / "src" / "test" / "resources" / "api-v1"
URL_BASE = sys.argv[1] if len(sys.argv) > 1 else "https://astrolabio-api.vercel.app/api/v1/"


def descargar(ruta: str) -> dict:
    """GET {URL_BASE}{ruta}; guarda la respuesta como zonas?tipo=casa -> zonas.tipo-casa.json."""
    resultado = subprocess.run(
        ["curl", "-s", "--fail-with-body", URL_BASE.rstrip("/") + "/" + ruta],
        capture_output=True,
        check=False,
    )
    cuerpo = resultado.stdout.decode("utf-8")
    if resultado.returncode != 0:
        raise SystemExit(f"Falló GET {ruta}: {cuerpo[:200]}")
    archivo = SALIDA / (ruta.replace("?", ".").replace("=", "-") + ".json")
    archivo.parent.mkdir(parents=True, exist_ok=True)
    archivo.write_text(cuerpo, encoding="utf-8")
    return json.loads(cuerpo)["data"]


def main():
    if SALIDA.exists():
        shutil.rmtree(SALIDA)
    for ruta in ["salud", "contenido/version", "mapa", "mecanicas",
                 "zonas?tipo=eje", "zonas?tipo=casa", "zonas?tipo=cierre"]:
        descargar(ruta)

    for seccion in descargar("historia"):
        for entrada in seccion["entradas"]:
            descargar(f"historia/{entrada['slug']}")
    for zona in descargar("zonas"):
        for sala in descargar(f"zonas/{zona['slug']}")["salas"]:
            descargar(f"salas/{sala['slug']}")
    for personaje in descargar("personajes"):
        descargar(f"personajes/{personaje['slug']}")
    for categoria in descargar("archivos"):
        for archivo in categoria["elementos"]:
            descargar(f"archivos/{archivo['slug']}")

    total = sum(1 for _ in SALIDA.rglob("*.json"))
    print(f"{total} respuestas guardadas en {SALIDA.relative_to(RAIZ_REPO)}")


if __name__ == "__main__":
    main()
