package com.edu.ucne.parrilladalos2carnales.domain.model.notificacion


enum class TipoNotificacion {
    PEDIDO,
    OFERTA,
    SISTEMA
}


enum class DestinoNotificacion {
    CLIENTE,
    ADMINISTRADOR
}


data class Notificacion(
    val id: Long = 0L,


    val titulo: String = "",


    val mensaje: String = "",


    val tipo: TipoNotificacion =
        TipoNotificacion.SISTEMA,


    val destino: DestinoNotificacion =
        DestinoNotificacion.CLIENTE,


    /*
     * Para cliente:
     * aquí guardamos el UID de Firebase.
     *
     * Para administrador puede ser null.
     */
    val usuarioUid: String? = null,


    val fecha: Long =
        System.currentTimeMillis(),


    val leida: Boolean = false,


    val idReferencia: Int? = null
)
