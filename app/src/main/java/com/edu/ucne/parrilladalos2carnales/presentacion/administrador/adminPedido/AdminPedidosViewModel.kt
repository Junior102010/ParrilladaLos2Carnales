package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.TipoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
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
    private val pedidoRepository: PedidoRepository,
    private val notificacionRepository: NotificacionRepository
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
            try {
                pedidoRepository.getPedidos().collect { lista ->
                    _uiState.update { it.copy(pedidos = lista, isLoading = false) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(pedidos = emptyList(), isLoading = false) }
            }
        }
    }

    private fun cambiarEstadoPedido(
        idPedido: Int,
        nuevoEstado: EstadoPedido
    ) {
        viewModelScope.launch {
            val pedidoActual =
                _uiState.value.pedidos
                    .find {
                        it.idPedido == idPedido
                    }
                    ?: return@launch

            val pedidoActualizado =
                pedidoActual.copy(
                    estado = nuevoEstado
                )

            pedidoRepository
                .upsertPedido(
                    pedidoActualizado
                )

            _uiState.update { state ->
                state.copy(
                    pedidos =
                        state.pedidos.map {
                            if (it.idPedido == idPedido) {
                                pedidoActualizado
                            } else {
                                it
                            }
                        }
                )
            }

            val mensaje = when (nuevoEstado) {
                EstadoPedido.RECIBIDO -> "Tu pedido #$idPedido fue recibido."
                EstadoPedido.PREPARANDO -> "Tu pedido #$idPedido ya está siendo preparado."
                EstadoPedido.EN_CAMINO -> "Tu pedido #$idPedido va en camino."
                EstadoPedido.ENTREGADO -> "Tu pedido #$idPedido fue entregado. ¡Buen provecho!"
                EstadoPedido.CANCELADO -> "Tu pedido #$idPedido fue cancelado."
            }

            notificacionRepository.agregar(
                Notificacion(
                    titulo = "Actualización del pedido",
                    mensaje = mensaje,
                    tipo = TipoNotificacion.PEDIDO,
                    destino = DestinoNotificacion.CLIENTE,
                    usuarioUid = pedidoActual.usuarioUid,
                    idReferencia = pedidoActual.idPedido
                )
            )
        }
    }
}
