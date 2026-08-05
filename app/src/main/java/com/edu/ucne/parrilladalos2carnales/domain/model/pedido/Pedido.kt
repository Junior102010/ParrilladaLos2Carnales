package com.edu.ucne.parrilladalos2carnales.domain.model.pedido


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Pedido(
    val idPedido: Int = 0,
    val idUsuario: Int = 0,
    val clienteNombre: String = "",
    val fecha: String = obtenerFechaHoy(),
    val fechaMillis: Long = System.currentTimeMillis(),
    val total: Double = 0.0,
    val estado: EstadoPedido = EstadoPedido.PENDIENTE,
    val detalles: List<DetallePedido> = emptyList()
)

fun obtenerFechaHoy(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(Date())
}