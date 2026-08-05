package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido


import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

data class AdminPedidosUiState(
    val isLoading: Boolean = false,
    val pedidos: List<Pedido> = emptyList(),
    val filtroEstado: EstadoPedido? = null,
    val searchQuery: String = "",
    val errorMessage: String? = null
)