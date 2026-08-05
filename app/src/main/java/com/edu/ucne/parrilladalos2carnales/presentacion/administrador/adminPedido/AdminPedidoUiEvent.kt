package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido

import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido

sealed interface AdminPedidosUiEvent {
    data class OnSearchQueryChanged(val query: String) : AdminPedidosUiEvent
    data class OnFiltrarPorEstado(val estado: EstadoPedido?) : AdminPedidosUiEvent
    data class OnCambiarEstadoPedido(val idPedido: Int, val nuevoEstado: EstadoPedido) : AdminPedidosUiEvent
    data object OnRefrescar : AdminPedidosUiEvent
}