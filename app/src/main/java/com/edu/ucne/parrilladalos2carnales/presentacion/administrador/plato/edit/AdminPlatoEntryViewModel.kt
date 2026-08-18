package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria.GetCategoriasUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria.UpsertCategoriaUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class AdminPlatoEntryViewModel @Inject constructor(
    private val getPlatoUseCase: GetPlatoUseCase,
    private val upsertPlatoUseCase: UpsertPlatoUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase,
    private val upsertCategoriaUseCase: UpsertCategoriaUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPlatoEntryUiState())
    val uiState: StateFlow<AdminPlatoEntryUiState> = _uiState.asStateFlow()

    init {
        cargarCategorias()
    }

    private fun cargarCategorias() {
        viewModelScope.launch {
            try {
                val categoriasActuales = getCategoriasUseCase().first()

                if (categoriasActuales.isEmpty()) {
                    val categoriasIniciales = listOf(
                        Categoria(
                            nombreCategoria = "Parrilladas",
                            descripcionCategoria = "Platos completos preparados a la parrilla"
                        ),
                        Categoria(
                            nombreCategoria = "Cortes",
                            descripcionCategoria = "Cortes de carne preparados al gusto"
                        ),
                        Categoria(
                            nombreCategoria = "Bebidas",
                            descripcionCategoria = "Bebidas disponibles en el menú"
                        ),
                        Categoria(
                            nombreCategoria = "Combos",
                            descripcionCategoria = "Combinaciones y especiales de la casa"
                        )
                    )

                    categoriasIniciales.forEach { categoria ->
                        upsertCategoriaUseCase(categoria)
                    }
                }

                getCategoriasUseCase().collect { categorias ->
                    _uiState.update { state ->
                        state.copy(
                            categorias = categorias,
                            idCategoria = if (state.idCategoria <= 0 && categorias.isNotEmpty()) {
                                categorias.first().idCategoria
                            } else {
                                state.idCategoria
                            }
                        )
                    }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(
                        errorMessage = error.localizedMessage
                            ?: "No se pudieron cargar las categorías"
                    )
                }
            }
        }
    }

    fun prepararEntrada(idPlato: Int) {
        if (idPlato == 0) {
            _uiState.update { state ->
                AdminPlatoEntryUiState(
                    categorias = state.categorias,
                    idCategoria = state.categorias.firstOrNull()?.idCategoria ?: 0
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    isSuccess = false,
                    errorMessage = null,
                    nombreError = null,
                    precioError = null,
                    isLoading = true
                )
            }
            cargarPlatoParaEdicion(idPlato)
        }
    }

    fun consumirGuardadoExitoso() {
        _uiState.update {
            it.copy(
                isSuccess = false
            )
        }
    }

    fun onEvent(event: AdminPlatoEntryUiEvent) {
        when (event) {
            is AdminPlatoEntryUiEvent.OnNombreChanged -> {
                _uiState.update { it.copy(nombre = event.nombre, nombreError = null, errorMessage = null) }
            }
            is AdminPlatoEntryUiEvent.OnDescripcionChanged -> {
                _uiState.update { it.copy(descripcion = event.descripcion, errorMessage = null) }
            }
            is AdminPlatoEntryUiEvent.OnPrecioChanged -> {
                _uiState.update { it.copy(precio = event.precio, precioError = null, errorMessage = null) }
            }
            is AdminPlatoEntryUiEvent.OnCategoriaChanged -> {
                _uiState.update { it.copy(idCategoria = event.idCategoria, errorMessage = null) }
            }
            is AdminPlatoEntryUiEvent.OnDisponibleChanged -> {
                _uiState.update { it.copy(disponible = event.disponible) }
            }
            is AdminPlatoEntryUiEvent.OnImagenSelected -> {
                copiarYGuardarImagen(event.uriString)
            }
            is AdminPlatoEntryUiEvent.OnSave -> savePlato()
            AdminPlatoEntryUiEvent.ResetSuccess -> consumirGuardadoExitoso()
        }
    }

    private fun cargarPlatoParaEdicion(idPlato: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPlatoUseCase(idPlato).collect { plato ->
                if (plato != null) {
                    _uiState.update {
                        it.copy(
                            idPlato = plato.idPlato,
                            nombre = plato.nombre,
                            descripcion = plato.descripcion,
                            precio = plato.precio.toString(),
                            idCategoria = plato.idCategoria,
                            disponible = plato.disponible,
                            imagenUrl = plato.imagenUrl,
                            isLoading = false
                        )
                    }
                } else {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    private fun copiarYGuardarImagen(uriString: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val rutaLocal = withContext(Dispatchers.IO) {
                try {
                    val uri = Uri.parse(uriString)
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val archivoDestino = File(context.filesDir, "plato_${System.currentTimeMillis()}.jpg")
                    val outputStream = FileOutputStream(archivoDestino)
                    inputStream?.use { input ->
                        outputStream.use { output ->
                            input.copyTo(output)
                        }
                    }
                    archivoDestino.absolutePath
                } catch (e: Exception) {
                    null
                }
            }
            if (rutaLocal != null) {
                _uiState.update { it.copy(isLoading = false, imagenUrl = rutaLocal, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Error al procesar la imagen") }
            }
        }
    }

    private fun savePlato() {
        val state = _uiState.value
        val nombreVal = validateNombrePlato(state.nombre)
        val precioDouble = state.precio.toDoubleOrNull() ?: 0.0
        val precioVal = validatePrecioPlato(precioDouble)
        val categoriaVal = validateCategoriaPlato(state.idCategoria)
        val descripcionVal = validateDescripcionPlato(state.descripcion)

        if (!nombreVal.isValid || !precioVal.isValid || !categoriaVal.isValid || !descripcionVal.isValid) {
            _uiState.update {
                it.copy(
                    nombreError = nombreVal.errorMessage,
                    precioError = precioVal.errorMessage,
                    errorMessage = nombreVal.errorMessage 
                        ?: precioVal.errorMessage 
                        ?: categoriaVal.errorMessage 
                        ?: descripcionVal.errorMessage
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val plato = Plato(
                idPlato = state.idPlato,
                nombre = state.nombre,
                descripcion = state.descripcion,
                precio = precioDouble,
                idCategoria = state.idCategoria,
                imagenUrl = state.imagenUrl,
                disponible = state.disponible
            )
            val result = upsertPlatoUseCase(plato)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Error al guardar el plato") }
            }
        }
    }
}
