package com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion

import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

data class ConfirmacionPedidoUiState(
    val isLoading: Boolean = true,
    val pedido: Pedido? = null,
    val errorMessage: String? = null
)

