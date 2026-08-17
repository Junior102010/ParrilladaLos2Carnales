package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPerfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPerfilUiState())
    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = Channel<Unit>(capacity = Channel.BUFFERED)
    val logoutEvent = _logoutEvent.receiveAsFlow()

    init {
        refrescarUsuario()
    }

    fun refrescarUsuario() {
        _uiState.update { state ->
            state.copy(
                nombre = authRepository
                    .getNombreUsuario()
                    ?.takeIf { it.isNotBlank() }
                    ?: "Administrador",
                correo = authRepository
                    .getCorreoUsuario()
                    .orEmpty(),
                fotoUrl = authRepository.getFotoUsuario()
            )
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            authRepository.cerrarSesion()
            _logoutEvent.send(Unit)
        }
    }
}
