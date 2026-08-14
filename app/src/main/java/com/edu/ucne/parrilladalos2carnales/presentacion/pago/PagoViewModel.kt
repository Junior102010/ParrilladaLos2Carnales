package com.edu.ucne.parrilladalos2carnales.presentacion.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.DetallePedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PagoViewModel @Inject constructor(
    private val carritoRepository: CarritoRepository,
    private val pedidoRepository: PedidoRepository,
    private val authRepository: AuthRepository
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
            PagoUiEvent.OnConfirmarPago -> confirmarPedido()
            PagoUiEvent.OnPedidoCreadoConsumido -> _uiState.update { it.copy(pedidoCreadoId = null) }
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

    private fun confirmarPedido() {
        val state = _uiState.value
        if (state.subtotal <= 0.0) return mostrarError("El carrito está vacío")
        if (state.tipoEntrega == TipoEntrega.DELIVERY && state.direccion.isBlank()) return mostrarError("Debes indicar una dirección")

        when (state.metodoPago) {
            MetodoPago.TARJETA -> {
                if (state.numeroTarjeta.length != 16) return mostrarError("Introduce un número de tarjeta válido")
                if (!fechaValida(state.fechaTarjeta)) return mostrarError("Introduce una fecha válida")
                if (state.cvv.length !in 3..4) return mostrarError("Introduce un CVV válido")
            }
            MetodoPago.EFECTIVO -> {
                val monto = state.montoRecibido.toDoubleOrNull()
                if (monto == null || monto < state.total) return mostrarError("El monto recibido es insuficiente")
            }
            MetodoPago.TRANSFERENCIA -> {
                if (state.titularTransferencia.isBlank() || state.cuentaOrigen.isBlank() || state.banco.isBlank()) {
                    return mostrarError("Completa los datos de transferencia")
                }
            }
        }
        crearPedido()
    }

    private fun crearPedido() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val carrito = carritoRepository.observeCarrito().first()
                if (carrito.isEmpty()) return@launch mostrarError("El carrito está vacío")

                val detalles = carrito.map { item ->
                    DetallePedido(
                        idPlato = item.plato.idPlato,
                        nombrePlato = item.plato.nombre,
                        imagenUrl = item.plato.imagenUrl,
                        cantidad = item.cantidad,
                        precioUnitario = item.precioUnitario,
                        subtotal = item.subtotal,
                        termino = item.termino?.nombreComponente.orEmpty(),
                        idTermino = item.termino?.idComponente,
                        guarnicion = item.guarnicion?.nombreGuarnicion.orEmpty(),
                        idGuarnicion = item.guarnicion?.idGuarnicion,
                        salsa = item.salsa?.nombreComponente.orEmpty(),
                        idSalsa = item.salsa?.idComponente
                    )
                }

                val tiempo = if (_uiState.value.tipoEntrega == TipoEntrega.DELIVERY) "35 - 45 min" else "20 - 30 min"

                val pedido = Pedido(
                    usuarioUid = authRepository.getUsuarioUid().orEmpty(),
                    clienteNombre = authRepository.getNombreUsuario() ?: "Cliente",
                    subtotal = _uiState.value.subtotal,
                    costoDelivery = _uiState.value.delivery,
                    total = _uiState.value.total,
                    tipoEntrega = _uiState.value.tipoEntrega.name,
                    direccion = if (_uiState.value.tipoEntrega == TipoEntrega.DELIVERY) _uiState.value.direccion else "Recoger en el local",
                    metodoPago = _uiState.value.metodoPago.name,
                    tiempoEstimado = tiempo,
                    detalles = detalles
                )

                val idPedido = pedidoRepository.upsertPedido(pedido)
                carritoRepository.vaciar()

                _uiState.update { it.copy(isLoading = false, pedidoCreadoId = idPedido) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "No se pudo crear el pedido") }
            }
        }
    }

    private fun mostrarError(mensaje: String) = _uiState.update { it.copy(errorMessage = mensaje, isLoading = false) }

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
