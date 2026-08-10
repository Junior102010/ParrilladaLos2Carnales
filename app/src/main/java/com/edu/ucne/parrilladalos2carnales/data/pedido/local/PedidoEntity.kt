package com.edu.ucne.parrilladalos2carnales.data.pedido.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pedidos")
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true)
    val idPedido: Int = 0,
    val idUsuario: Int = 0,
    val usuarioUid: String = "",
    val clienteNombre: String = "",
    val fecha: String = "",
    val fechaMillis: Long = 0,
    val subtotal: Double = 0.0,
    val costoDelivery: Double = 0.0,
    val total: Double = 0.0,
    val tipoEntrega: String = "",
    val direccion: String = "",
    val metodoPago: String = "",
    val tiempoEstimado: String = "",
    val estado: String = ""
)
