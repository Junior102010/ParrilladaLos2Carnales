package com.edu.ucne.parrilladalos2carnales.domain.model.pedido

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Pedido(
    val idPedido: Int = 0,
    // Se mantiene por compatibilidad.
    val idUsuario: Int = 0,
    // ID verdadero del usuario Firebase.
    val usuarioUid: String = "",
    val clienteNombre: String = "",
    val fecha: String = obtenerFechaHoy(),
    val fechaMillis: Long = System.currentTimeMillis(),
    val subtotal: Double = 0.0,
    val costoDelivery: Double = 0.0,
    val total: Double = 0.0,
    // DELIVERY / RECOGER
    val tipoEntrega: String = "",
    val direccion: String = "",
    // EFECTIVO / TARJETA / TRANSFERENCIA
    val metodoPago: String = "",
    val tiempoEstimado: String = "",
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val detalles: List<DetallePedido> = emptyList()
) {
    val numeroOrden: String
        get() = "ORD-${idPedido.toString().padStart(5, '0')}"
}

fun obtenerFechaHoy(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}
