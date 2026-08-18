package com.edu.ucne.parrilladalos2carnales.presentacion.notificacion

import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion

data class NotificacionUiState(

    val notificaciones:
        List<Notificacion> =
        emptyList(),

    val isLoading:
        Boolean = false
) {

    val noLeidas: Int
        get() =
            notificaciones.count {
                !it.leida
            }
}
