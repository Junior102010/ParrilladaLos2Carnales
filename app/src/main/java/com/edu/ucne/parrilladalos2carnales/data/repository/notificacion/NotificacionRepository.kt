package com.edu.ucne.parrilladalos2carnales.data.repository.notificacion

import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificacionRepository @Inject constructor() {

    private val _notificaciones =
        MutableStateFlow<List<Notificacion>>(
            emptyList()
        )

    val notificaciones:
        StateFlow<List<Notificacion>> =
        _notificaciones.asStateFlow()

    fun agregar(
        notificacion: Notificacion
    ) {

        _notificaciones.update { actual ->

            listOf(
                notificacion.copy(
                    id =
                        System.currentTimeMillis()
                )
            ) + actual
        }
    }

    fun marcarComoLeida(
        id: Long
    ) {

        _notificaciones.update { lista ->

            lista.map {

                if (it.id == id) {
                    it.copy(
                        leida = true
                    )
                } else {
                    it
                }
            }
        }
    }

    fun marcarTodasComoLeidas() {

        _notificaciones.update { lista ->

            lista.map {
                it.copy(
                    leida = true
                )
            }
        }
    }

    fun eliminar(
        id: Long
    ) {

        _notificaciones.update { lista ->

            lista.filterNot {
                it.id == id
            }
        }
    }
}
