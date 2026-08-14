package com.edu.ucne.parrilladalos2carnales.presentacion.seguimiento

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
class SeguimientoViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SeguimientoUiState())
    val uiState = _uiState.asStateFlow()

    private var pedidoJob: Job? = null

    fun setPedidoId(idPedido: Int) {
        pedidoJob?.cancel()
        pedidoJob = viewModelScope.launch {
            pedidoRepository.getPedido(idPedido)
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage ?: "No se pudo cargar el pedido"
                        )
                    }
                }
                .collect { pedido ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            pedido = pedido,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}
