package com.edu.ucne.parrilladalos2carnales.presentacion.seguimiento

import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

data class SeguimientoUiState(
    val isLoading: Boolean = true,
    val pedido: Pedido? = null,
    val errorMessage: String? = null
)
