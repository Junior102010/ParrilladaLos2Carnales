package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPedidosViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPedidosUiState())
    val uiState: StateFlow<AdminPedidosUiState> = _uiState.asStateFlow()

    init {
        cargarPedidos()
    }

    fun onEvent(event: AdminPedidosUiEvent) {
        when (event) {
            is AdminPedidosUiEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
            }
            is AdminPedidosUiEvent.OnFiltrarPorEstado -> {
                _uiState.update { it.copy(filtroEstado = event.estado) }
            }
            is AdminPedidosUiEvent.OnCambiarEstadoPedido -> {
                cambiarEstadoPedido(event.idPedido, event.nuevoEstado)
            }
            AdminPedidosUiEvent.OnRefrescar -> {
                cargarPedidos()
            }
        }
    }

    private fun cargarPedidos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            if (pedidoRepository != null) {
                try {
                    pedidoRepository.getPedidos().collect { lista ->
                        _uiState.update { it.copy(pedidos = lista, isLoading = false) }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(pedidos = emptyList(), isLoading = false) }
                }
            } else {
                _uiState.update { it.copy(pedidos = emptyList(), isLoading = false) }
            }
        }
    }

    private fun cambiarEstadoPedido(idPedido: Int, nuevoEstado: EstadoPedido) {
        viewModelScope.launch {
            val actualizados = _uiState.value.pedidos.map { pedido ->
                if (pedido.idPedido == idPedido) pedido.copy(estado = nuevoEstado) else pedido
            }
            _uiState.update { it.copy(pedidos = actualizados) }
            pedidoRepository?.let { repo ->
                actualizados.find { it.idPedido == idPedido }?.let { repo.upsertPedido(it) }
            }
        }
    }
}
