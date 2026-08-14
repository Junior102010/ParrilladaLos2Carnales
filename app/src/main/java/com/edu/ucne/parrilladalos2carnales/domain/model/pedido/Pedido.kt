package com.edu.ucne.parrilladalos2carnales.domain.model.pedido

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Pedido(
    val idPedido: Int = 0,

    val idUsuario: Int = 0,

    val usuarioUid: String = "",
    val clienteNombre: String = "",
    val fecha: String = obtenerFechaHoy(),
    val fechaMillis: Long = System.currentTimeMillis(),
    val subtotal: Double = 0.0,
    val costoDelivery: Double = 0.0,
    val total: Double = 0.0,

    val tipoEntrega: String = "",
    val direccion: String = "",

    val metodoPago: String = "",
    val tiempoEstimado: String = "",
    val estado: EstadoPedido = EstadoPedido.RECIBIDO,
    val detalles: List<DetallePedido> = emptyList()
) {
    val numeroOrden: String
        get() = "#${idPedido.toString().padStart(5, '0')}"
}

fun obtenerFechaHoy(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}

