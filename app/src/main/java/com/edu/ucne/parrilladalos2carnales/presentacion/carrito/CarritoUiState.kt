package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem

data class CarritoUiState(
    val items: List<CarritoItem> =
        emptyList(),

    val subtotal: Double = 0.0,

    val delivery: Double = 0.0,

    val total: Double = 0.0
)
