package com.edu.ucne.parrilladalos2carnales.data.repository.notificacion

import com.edu.ucne.parrilladalos2carnales.data.notification.AndroidNotificationService
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacionRepository @Inject constructor(
    private val notificationService: AndroidNotificationService
) {
    private val _notificaciones = MutableStateFlow<List<Notificacion>>(emptyList())
    val notificaciones: StateFlow<List<Notificacion>> = _notificaciones.asStateFlow()

    fun agregar(notificacion: Notificacion) {
        val nueva = notificacion.copy(id = System.currentTimeMillis())
        _notificaciones.update { actual ->
            listOf(nueva) + actual
        }
        notificationService.mostrar(nueva)
    }

    fun mostrarPendientesSesion(usuarioUid: String?, esAdministrador: Boolean) {
        val pendientes = _notificaciones.value.filter { noti ->
            !noti.leida && (
                (esAdministrador && noti.destino == DestinoNotificacion.ADMINISTRADOR) ||
                (!esAdministrador && noti.destino == DestinoNotificacion.CLIENTE && noti.usuarioUid == usuarioUid)
            )
        }
        pendientes.forEach { notificationService.mostrar(it) }
    }

    fun marcarComoLeida(id: Long) {
        _notificaciones.update { lista ->
            lista.map {
                if (it.id == id) it.copy(leida = true) else it
            }
        }
    }

    fun marcarTodasComoLeidas(ids: List<Long>) {
        val idsSet = ids.toSet()
        _notificaciones.update { lista ->
            lista.map {
                if (it.id in idsSet) it.copy(leida = true) else it
            }
        }
    }

    fun eliminar(id: Long) {
        _notificaciones.update { lista ->
            lista.filterNot { it.id == id }
        }
    }
}
