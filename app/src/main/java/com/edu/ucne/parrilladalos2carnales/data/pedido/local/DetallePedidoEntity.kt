package com.edu.ucne.parrilladalos2carnales.data.pedido.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "DetallesPedido")
data class DetallePedidoEntity(
    @PrimaryKey(autoGenerate = true)
    val idDetalle: Int = 0,
    val idPedido: Int = 0,
    val idPlato: Int = 0,
    val nombrePlato: String = "",
    val cantidad: Int = 0,
    val precioUnitario: Double = 0.0,
    val subtotal: Double = 0.0,
    val termino: String = "",
    val guarnicion: String = "",
    val salsa: String = ""
)
