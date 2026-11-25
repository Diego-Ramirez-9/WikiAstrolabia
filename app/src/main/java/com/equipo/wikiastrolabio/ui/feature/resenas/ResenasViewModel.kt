package com.equipo.wikiastrolabio.ui.feature.resenas

import androidx.lifecycle.viewModelScope
import com.equipo.wikiastrolabio.domain.model.ErrorDatos
import com.equipo.wikiastrolabio.domain.model.NuevaResena
import com.equipo.wikiastrolabio.domain.model.ReglasResena
import com.equipo.wikiastrolabio.domain.model.Resultado
import com.equipo.wikiastrolabio.domain.model.ResumenResenas
import com.equipo.wikiastrolabio.domain.usecase.resenas.ObtenerResenasUseCase
import com.equipo.wikiastrolabio.domain.usecase.resenas.PublicarResenaUseCase
import com.equipo.wikiastrolabio.ui.common.ContenidoViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Estado del formulario "Escribe tu reseña". */
data class FormularioResena(
    val apodo: String = "",
    /** 0 = aún no elige estrellas. */
    val calificacion: Int = 0,
    val texto: String = "",
    val enviando: Boolean = false,
    /** Error del último envío (se borra al editar). */
    val error: ErrorDatos? = null,
    /** La última reseña se publicó; se muestra el agradecimiento hasta que vuelva a escribir. */
    val publicada: Boolean = false,
) {
    val valido: Boolean get() = ReglasResena.esValida(apodo, calificacion, texto)
    val puedeEnviar: Boolean get() = valido && !enviando
}

@HiltViewModel
class ResenasViewModel @Inject constructor(
    private val obtenerResenas: ObtenerResenasUseCase,
    private val publicarResena: PublicarResenaUseCase,
) : ContenidoViewModel<ResumenResenas>() {

    private val _formulario = MutableStateFlow(FormularioResena())
    val formulario: StateFlow<FormularioResena> = _formulario.asStateFlow()

    init {
        cargar()
        viewModelScope.launch {
            val apodo = publicarResena.apodoGuardado().orEmpty()
            _formulario.update { if (it.apodo.isEmpty()) it.copy(apodo = apodo) else it }
        }
    }

    override suspend fun obtener(forzarRed: Boolean) = obtenerResenas()

    fun cambiarApodo(apodo: String) = editar { it.copy(apodo = apodo.take(ReglasResena.APODO.last * 2)) }

    fun cambiarCalificacion(calificacion: Int) = editar { it.copy(calificacion = calificacion) }

    fun cambiarTexto(texto: String) = editar { it.copy(texto = texto.take(ReglasResena.TEXTO.last * 2)) }

    fun publicar() {
        val actual = _formulario.value
        if (!actual.puedeEnviar) return
        _formulario.value = actual.copy(enviando = true, error = null)
        viewModelScope.launch {
            when (val resultado = publicarResena(NuevaResena(actual.apodo, actual.calificacion, actual.texto))) {
                is Resultado.Exito -> {
                    // Se conserva el apodo (ya normalizado por la API) para la próxima reseña.
                    _formulario.value = FormularioResena(apodo = resultado.datos.apodo, publicada = true)
                    recargar()
                }
                is Resultado.Error -> _formulario.update { it.copy(enviando = false, error = resultado.error) }
            }
        }
    }

    // Cualquier edición borra el error anterior y el agradecimiento.
    private fun editar(cambio: (FormularioResena) -> FormularioResena) {
        _formulario.update { if (it.enviando) it else cambio(it).copy(error = null, publicada = false) }
    }
}
