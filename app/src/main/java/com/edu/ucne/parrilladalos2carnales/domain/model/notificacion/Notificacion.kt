package com.edu.ucne.parrilladalos2carnales.domain.model.notificacion

enum class TipoNotificacion {
    PEDIDO,
    OFERTA,
    SISTEMA
}

data class Notificacion(
    val id: Long = 0L,
    val titulo: String = "",
    val mensaje: String = "",
    val tipo: TipoNotificacion = TipoNotificacion.SISTEMA,
    val fecha: Long = System.currentTimeMillis(),
    val leida: Boolean = false,

    // Para poder abrir una pantalla específica
    val idReferencia: Int? = null
)
