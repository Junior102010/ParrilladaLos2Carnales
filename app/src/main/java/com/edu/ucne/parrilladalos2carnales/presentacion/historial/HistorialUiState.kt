package com.edu.ucne.parrilladalos2carnales.presentacion.historial

import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

data class HistorialUiState(
    val isLoading: Boolean = true,
    val pedidos: List<Pedido> = emptyList(),
    val errorMessage: String? = null
)

