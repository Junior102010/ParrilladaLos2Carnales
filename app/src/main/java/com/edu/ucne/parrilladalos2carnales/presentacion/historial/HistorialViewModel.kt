package com.edu.ucne.parrilladalos2carnales.presentacion.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState = _uiState.asStateFlow()

    init {
        cargarHistorial()
    }

    private fun cargarHistorial() {
        val uid = authRepository.getUsuarioUid()
        if (uid.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "No hay una sesión iniciada") }
            return
        }
        viewModelScope.launch {
            pedidoRepository.getPedidosPorUsuario(uid)
                .catch { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "No se pudo cargar el historial")
                    }
                }
                .collect { pedidos ->
                    _uiState.update {
                        it.copy(isLoading = false, pedidos = pedidos, errorMessage = null)
                    }
                }
        }
    }
}

