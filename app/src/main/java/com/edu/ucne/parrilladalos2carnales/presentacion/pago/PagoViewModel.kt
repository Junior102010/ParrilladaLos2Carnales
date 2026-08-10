package com.edu.ucne.parrilladalos2carnales.presentacion.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PagoUiState())
    val uiState: StateFlow<PagoUiState> = _uiState.asStateFlow()

    init { observarCarrito() }

    fun onEvent(event: PagoUiEvent) {
        when (event) {
            is PagoUiEvent.OnTipoEntregaChange -> {
                val costo = if (event.tipo == TipoEntrega.DELIVERY) 100.0 else 0.0
                _uiState.update { it.copy(tipoEntrega = event.tipo, delivery = costo, total = it.subtotal + costo, errorMessage = null) }
            }
            is PagoUiEvent.OnDireccionChange -> _uiState.update { it.copy(direccion = event.direccion, errorMessage = null) }
            is PagoUiEvent.OnMetodoPagoChange -> _uiState.update { it.copy(metodoPago = event.metodo, errorMessage = null) }
            is PagoUiEvent.OnNumeroTarjetaChange -> {
                val num = event.numero.filter { it.isDigit() }.take(16)
                _uiState.update { it.copy(numeroTarjeta = num, errorMessage = null) }
            }
            is PagoUiEvent.OnFechaTarjetaChange -> _uiState.update { it.copy(fechaTarjeta = formatearFecha(event.fecha), errorMessage = null) }
            is PagoUiEvent.OnCvvChange -> {
                val cvv = event.cvv.filter { it.isDigit() }.take(4)
                _uiState.update { it.copy(cvv = cvv, errorMessage = null) }
            }
            is PagoUiEvent.OnMontoRecibidoChange -> _uiState.update { it.copy(montoRecibido = event.monto.filter { it.isDigit() || it == '.' }, errorMessage = null) }
            is PagoUiEvent.OnTitularTransferenciaChange -> _uiState.update { it.copy(titularTransferencia = event.titular, errorMessage = null) }
            is PagoUiEvent.OnCuentaOrigenChange -> _uiState.update { it.copy(cuentaOrigen = event.cuenta, errorMessage = null) }
            is PagoUiEvent.OnBancoChange -> _uiState.update { it.copy(banco = event.banco, errorMessage = null) }
            PagoUiEvent.OnConfirmarPago -> validarPago()
            PagoUiEvent.OnValidacionConsumida -> _uiState.update { it.copy(datosPagoValidos = false) }
        }
    }

    private fun observarCarrito() {
        viewModelScope.launch {
            carritoRepository.observeCarrito().collect { items ->
                val subtotal = items.sumOf { it.subtotal }
                _uiState.update { it.copy(subtotal = subtotal, total = subtotal + it.delivery) }
            }
        }
    }

    private fun validarPago() {
        val state = _uiState.value
        if (state.subtotal <= 0.0) return mostrarError("El carrito está vacío")
        if (state.tipoEntrega == TipoEntrega.DELIVERY && state.direccion.isBlank()) return mostrarError("Debes seleccionar una dirección")

        when (state.metodoPago) {
            MetodoPago.TARJETA -> {
                if (state.numeroTarjeta.length != 16) return mostrarError("Introduce un número de tarjeta válido")
                if (!fechaValida(state.fechaTarjeta)) return mostrarError("Introduce una fecha válida")
                if (state.cvv.length !in 3..4) return mostrarError("Introduce un CVV válido")
            }
            MetodoPago.EFECTIVO -> {
                val monto = state.montoRecibido.toDoubleOrNull()
                if (monto == null) return mostrarError("Indica con cuánto pagarás")
                if (monto < state.total) return mostrarError("El monto recibido no puede ser menor al total")
            }
            MetodoPago.TRANSFERENCIA -> {
                if (state.titularTransferencia.isBlank()) return mostrarError("Introduce el titular de la transferencia")
                if (state.cuentaOrigen.isBlank()) return mostrarError("Introduce la cuenta de origen")
                if (state.banco.isBlank()) return mostrarError("Selecciona o introduce el banco")
            }
        }
        _uiState.update { it.copy(errorMessage = null, datosPagoValidos = true) }
    }

    private fun mostrarError(mensaje: String) = _uiState.update { it.copy(errorMessage = mensaje, datosPagoValidos = false) }

    private fun formatearFecha(entrada: String): String {
        val num = entrada.filter { it.isDigit() }.take(4)
        return if (num.length <= 2) num else "${num.take(2)}/${num.drop(2)}"
    }

    private fun fechaValida(fecha: String): Boolean {
        if (fecha.length != 5 || fecha[2] != '/') return false
        val mes = fecha.substring(0, 2).toIntOrNull() ?: return false
        return mes in 1..12
    }
}
