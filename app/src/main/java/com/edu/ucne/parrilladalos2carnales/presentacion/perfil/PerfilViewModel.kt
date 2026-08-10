package com.edu.ucne.parrilladalos2carnales.presentacion.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarUsuario()
    }

    private fun cargarUsuario() {
        _uiState.update {
            it.copy(
                nombre = authRepository.getNombreUsuario() ?: "Cliente",
                correo = authRepository.getCorreoUsuario().orEmpty(),
                fotoUrl = authRepository.getFotoUsuario()
            )
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            carritoRepository.vaciar()
            authRepository.cerrarSesion()
            _uiState.update {
                it.copy(sesionCerrada = true)
            }
        }
    }
}

