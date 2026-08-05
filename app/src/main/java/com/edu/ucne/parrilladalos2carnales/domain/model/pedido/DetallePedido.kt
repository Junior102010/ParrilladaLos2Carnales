package com.edu.ucne.parrilladalos2carnales.domain.model.pedido


data class DetallePedido(
    val idDetalle: Int = 0,
    val idPedido: Int = 0,
    val idPlato: Int = 0,
    val nombrePlato: String = "",
    val cantidad: Int = 1,
    val precioUnitario: Double = 0.0,
    val subtotal: Double = cantidad * precioUnitario
)