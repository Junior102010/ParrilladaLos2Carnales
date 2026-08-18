package com.edu.ucne.parrilladalos2carnales.data.repository.notificacion

import com.edu.ucne.parrilladalos2carnales.data.notification.AndroidNotificationService
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacionRepository @Inject constructor(
    private val notificationService: AndroidNotificationService,
    private val authRepository: AuthRepository
) {
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones.asStateFlow()

    private val mostradasEnSistema = mutableSetOf<Long>()

    fun agregar(notificacion: Notificacion) {
        val nueva = notificacion.copy(id = System.currentTimeMillis())
        _notificaciones.update { actual ->
            listOf(nueva) + actual
        }
        mostrarSiCorresponde(nueva)
    }

    private fun mostrarSiCorresponde(notificacion: Notificacion) {
        val usuarioUid = authRepository.getUsuarioUid()
        val esAdministrador = authRepository.esAdministrador()

        val corresponde = if (esAdministrador) {
            notificacion.destino == DestinoNotificacion.ADMINISTRADOR
        } else {
            notificacion.destino == DestinoNotificacion.CLIENTE && notificacion.usuarioUid == usuarioUid
        }

        if (corresponde && !notificacion.leida && notificacion.id !in mostradasEnSistema) {
            notificationService.mostrar(notificacion)
            mostradasEnSistema.add(notificacion.id)
        }
    }

    fun mostrarPendientesSesion() {
        _notificaciones.value.forEach { notificacion ->
            mostrarSiCorresponde(notificacion)
        }
    }

    fun marcarComoLeida(id: Long) {
        _notificaciones.update { lista ->
            lista.map { notificacion ->
                if (notificacion.id == id) {
                    notificacion.copy(leida = true)
                } else {
                    notificacion
                }
            }
        }
    }

    fun marcarTodasComoLeidas(ids: List<Long>) {
        val idsSet = ids.toSet()
        _notificaciones.update { lista ->
            lista.map { notificacion ->
                if (notificacion.id in idsSet) {
                    notificacion.copy(leida = true)
                } else {
                    notificacion
                }
            }
        }
    }

    fun eliminar(id: Long) {
        _notificaciones.update { lista ->
            lista.filterNot {
                it.id == id
            }
        }
        mostradasEnSistema.remove(id)
    }

    fun limpiarNotificacionesSistema() {
        notificationService.cancelarTodas()
        mostradasEnSistema.clear()
    }
}
