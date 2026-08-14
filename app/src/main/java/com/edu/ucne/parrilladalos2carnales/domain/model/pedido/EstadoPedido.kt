package com.edu.ucne.parrilladalos2carnales.domain.model.pedido

enum class EstadoPedido(
    val descripcion: String,
    val paso: Int
) {
    RECIBIDO(
        descripcion = "Recibido",
        paso = 1
    ),
    PREPARANDO(
        descripcion = "Preparando",
        paso = 2
    ),
    EN_CAMINO(
        descripcion = "En Camino",
        paso = 3
    ),
    ENTREGADO(
        descripcion = "Entregado",
        paso = 4
    ),
    CANCELADO(
        descripcion = "Cancelado",
        paso = 0
    );

    val esActivo: Boolean
        get() = this != ENTREGADO && this != CANCELADO
}
