package com.edu.ucne.parrilladalos2carnales.presentacion.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val carritoRepository: CarritoRepository,
    private val notificacionRepository: NotificacionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState = _uiState.asStateFlow()

    private val _logoutEvent = Channel<Unit>(capacity = Channel.BUFFERED)
    val logoutEvent = _logoutEvent.receiveAsFlow()

    init {
        refrescarUsuario()
    }

    fun refrescarUsuario() {

        val esAdministrador =
            authRepository
                .esAdministrador()

        val nombre =
            authRepository
                .getNombreUsuario()
                .orEmpty()
                .trim()
                .ifBlank {

                    if (
                        esAdministrador
                    ) {
                        "Administrador"
                    } else {
                        "Cliente"
                    }
                }

        _uiState.update {

            it.copy(
                nombre =
                    nombre,

                correo =
                    authRepository
                        .getCorreoUsuario()
                        .orEmpty(),

                fotoUrl =
                    authRepository
                        .getFotoUsuario()
            )
        }
    }

    fun setNotificaciones(activa: Boolean) {
        _uiState.update {
            it.copy(notificacionesActivas = activa)
        }
    }

    fun cerrarSesion() {
        viewModelScope.launch {
            carritoRepository.vaciar()
            notificacionRepository.limpiarNotificacionesSistema()
            authRepository.cerrarSesion()
            _logoutEvent.send(Unit)
        }
    }
}
