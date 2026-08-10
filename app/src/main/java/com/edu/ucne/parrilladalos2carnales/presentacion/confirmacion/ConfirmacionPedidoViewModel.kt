package com.edu.ucne.parrilladalos2carnales.presentacion.confirmacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmacionPedidoViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmacionPedidoUiState())
    val uiState = _uiState.asStateFlow()

    private var cargarJob: Job? = null

    fun setId(idPedido: Int) {
        cargarJob?.cancel()
        cargarJob = viewModelScope.launch {
            pedidoRepository.getPedido(idPedido)
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage) }
                }
                .collect { pedido ->
                    _uiState.update { it.copy(isLoading = false, pedido = pedido) }
                }
        }
    }
}
