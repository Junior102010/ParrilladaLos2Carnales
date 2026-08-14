package com.edu.ucne.parrilladalos2carnales.domain.model.pedido

data class DetallePedido(
    val idDetalle: Int = 0,
    val idPedido: Int = 0,
    val idPlato: Int = 0,
    val nombrePlato: String = "",
    val imagenUrl: String = "",
    val cantidad: Int = 1,
    val precioUnitario: Double = 0.0,
    val subtotal: Double = cantidad * precioUnitario,
    val termino: String = "",
    val idTermino: Int? = null,
    val guarnicion: String = "",
    val idGuarnicion: Int? = null,
    val salsa: String = "",
    val idSalsa: Int? = null
)
