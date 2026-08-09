package com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfirmacionUiState(
    val isLoading: Boolean = true,
    val pedido: Pedido? = null
)

@HiltViewModel
class ConfirmacionPedidoViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmacionUiState())
    val uiState: StateFlow<ConfirmacionUiState> = _uiState.asStateFlow()

    fun cargarPedido(idPedido: Int) {
        viewModelScope.launch {
            pedidoRepository.getPedido(idPedido).collectLatest { pedido ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        pedido = pedido
                    )
                }
            }
        }
    }
}