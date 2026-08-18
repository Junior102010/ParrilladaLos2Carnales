package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class NotificacionViewModel @Inject constructor(
    private val repository: NotificacionRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val usuarioUid = authRepository.getUsuarioUid()
    private val esAdministrador = authRepository.esAdministrador()

    val uiState = repository.notificaciones
        .map { notificaciones ->
            val filtradas = notificaciones.filter { noti ->
                if (esAdministrador) {
                    noti.destino == DestinoNotificacion.ADMINISTRADOR
                } else {
                    noti.destino == DestinoNotificacion.CLIENTE && noti.usuarioUid == usuarioUid
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
        repository.mostrarPendientesSesion(
            usuarioUid = usuarioUid,
            esAdministrador = esAdministrador
        )
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
