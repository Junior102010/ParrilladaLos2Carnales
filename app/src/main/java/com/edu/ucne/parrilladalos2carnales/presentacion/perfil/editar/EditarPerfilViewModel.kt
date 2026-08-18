package com.edu.ucne.parrilladalos2carnales.presentacion.perfil.editar

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

@HiltViewModel
class EditarPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditarPerfilUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        _uiState.update {
            it.copy(
                nombre = authRepository.getNombreUsuario().orEmpty(),
                correo = authRepository.getCorreoUsuario().orEmpty(),
                fotoUrl = authRepository.getFotoUsuario(),
                rol = if (authRepository.esAdministrador()) "Administrador" else "Cliente"
            )
        }
    }

    fun onNombreChange(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, error = null) }
    }

    fun onFotoSelected(uri: Uri) {
        viewModelScope.launch {
            _uiState.update { it.copy(isProcessingImage = true, error = null) }
            val rutaLocal = withContext(Dispatchers.IO) {
                try {
                    val uid = authRepository.getUsuarioUid() ?: "anon"
                    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                    val archivoDestino = File(context.filesDir, "perfil_${uid}_${System.currentTimeMillis()}.jpg")
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
                _uiState.update { it.copy(isProcessingImage = false, fotoUrl = rutaLocal) }
            } else {
                _uiState.update { it.copy(isProcessingImage = false, error = "Error al procesar la imagen") }
            }
        }
    }

    fun guardarCambios() {
        val state = _uiState.value
        val nombre = state.nombre.trim()

        if (nombre.length < 2) {
            _uiState.update {
                it.copy(error = "El nombre debe tener al menos 2 caracteres")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = authRepository.actualizarPerfil(
                nombre = nombre,
                fotoUrl = state.fotoUrl
            )
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
            }.onFailure { error ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = error.message ?: "No se pudo actualizar el perfil"
                    )
                }
            }
        }
    }
}
