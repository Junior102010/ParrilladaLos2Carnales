package com.edu.ucne.parrilladalos2carnales.domain.model.pedido


enum class EstadoPedido(val descripcion: String) {
    PENDIENTE("Pendiente"),
    EN_PROCESO("En Proceso"),
    ENTREGADO("Entregado"),
    CANCELADO("Cancelado");

    // Pedidos activos: Pendientes o En proceso (no entregados ni cancelados)
    val esActivo: Boolean
        get() = this == PENDIENTE || this == EN_PROCESO
}