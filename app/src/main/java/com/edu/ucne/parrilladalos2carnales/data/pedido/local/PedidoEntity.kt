package com.edu.ucne.parrilladalos2carnales.data.pedido.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pedidos")
data class PedidoEntity(
    @PrimaryKey(autoGenerate = true)
    val idPedido: Int = 0,
    val idUsuario: Int = 0,
    val clienteNombre: String = "",
    val fecha: String = "",
    val fechaMillis: Long = 0,
    val total: Double = 0.0,
    val estado: String = ""
)
