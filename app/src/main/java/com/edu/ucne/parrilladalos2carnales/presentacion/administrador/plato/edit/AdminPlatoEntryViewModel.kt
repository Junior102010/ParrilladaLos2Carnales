package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatoUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.UpsertPlatoUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.validateNombrePlato
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.validatePrecioPlato
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPlatoEntryUiState())
    val uiState: StateFlow<AdminPlatoEntryUiState> = _uiState.asStateFlow()

    init {
        val platoId = savedStateHandle.get<Int>("idPlato") ?: savedStateHandle.get<Int>("platoId") ?: 0
        if (platoId != 0) {
            cargarPlatoParaEdicion(platoId)
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
            is AdminPlatoEntryUiEvent.OnImagenSelected -> {
                copiarYGuardarImagen(event.uriString)
            }
            is AdminPlatoEntryUiEvent.OnSave -> savePlato()
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

        if (!nombreVal.isValid || !precioVal.isValid) {
            _uiState.update {
                it.copy(
                    nombreError = nombreVal.errorMessage,
                    precioError = precioVal.errorMessage,
                    errorMessage = nombreVal.errorMessage ?: precioVal.errorMessage
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
                idCategoria = 1,
                imagenUrl = state.imagenUrl,
                disponible = true
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