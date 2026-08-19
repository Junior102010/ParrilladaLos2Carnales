package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class SesionNotificacion(
    val usuarioUid: String? = null,
    val esAdministrador: Boolean = false
)

@HiltViewModel
class NotificacionViewModel @Inject constructor(
    private val repository: NotificacionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val sesion = MutableStateFlow(obtenerSesionActual())

    val uiState = combine(
        repository.notificaciones,
        sesion
    ) { notificaciones, s ->
        val filtradas = notificaciones.filter { noti ->
            if (s.esAdministrador) {
                noti.destino == DestinoNotificacion.ADMINISTRADOR
            } else {
                noti.destino == DestinoNotificacion.CLIENTE && noti.usuarioUid == s.usuarioUid
            }
        }
        NotificacionUiState(notificaciones = filtradas)
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NotificacionUiState()
    )

    init {
        refrescarSesion()
    }

    private fun obtenerSesionActual(): SesionNotificacion {
        return SesionNotificacion(
            usuarioUid = authRepository.getUsuarioUid(),
            esAdministrador = authRepository.esAdministrador()
        )
    }

    fun refrescarSesion() {
        sesion.value = obtenerSesionActual()
        repository.mostrarPendientesSesion()
    }

    fun marcarLeida(id: Long) {
        repository.marcarComoLeida(id)
    }

    fun marcarTodasLeidas() {
        repository.marcarTodasComoLeidas(
            uiState.value.notificaciones.map { it.id }
        )
    }

    fun eliminar(id: Long) {
        repository.eliminar(id)
    }
}
