package com.edu.ucne.parrilladalos2carnales.presentacion.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.data.repository.notificacion.NotificacionRepository
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.DestinoNotificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.Notificacion
import com.edu.ucne.parrilladalos2carnales.domain.model.notificacion.TipoNotificacion
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
    private val authRepository: AuthRepository,
    private val notificacionRepository: NotificacionRepository
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
            is PagoUiEvent.OnMontoRecibidoChange -> _uiState.update { it.copy(montoRecibido = event.monto.filter { it.isDigit() || it == '.' }, errorMessage = null) }
            is PagoUiEvent.OnTitularTransferenciaChange -> _uiState.update { it.copy(titularTransferencia = event.titular, errorMessage = null) }
            is PagoUiEvent.OnBancoChange -> _uiState.update { it.copy(banco = event.banco, errorMessage = null) }
            is PagoUiEvent.OnReferenciaTransferenciaChange -> {
                val referencia = event.referencia.filter { it.isLetterOrDigit() || it == '-' || it == '_' }.take(30)
                _uiState.update { it.copy(referenciaTransferencia = referencia, errorMessage = null) }
            }
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
        if (state.isLoading) return
        if (state.subtotal <= 0.0) return mostrarError("El carrito está vacío")

        if (state.tipoEntrega == TipoEntrega.DELIVERY) {
            val direccion = state.direccion.trim()
            if (direccion.isBlank()) return mostrarError("Debes indicar la dirección de entrega")
            if (direccion.length < 8) return mostrarError("Escribe una dirección más completa")
            if (direccion.length > 150) return mostrarError("La dirección es demasiado larga")
        }

        when (state.metodoPago) {
            MetodoPago.EFECTIVO -> if (!validarPagoEfectivo(state)) return
            MetodoPago.TRANSFERENCIA -> if (!validarTransferencia(state)) return
        }
        crearPedido()
    }

    private fun validarPagoEfectivo(state: PagoUiState): Boolean {
        if (state.montoRecibido.isBlank()) {
            mostrarError("Indica con cuánto efectivo pagarás")
            return false
        }
        val monto = state.montoRecibido.toDoubleOrNull()
        if (monto == null) {
            mostrarError("Introduce un monto válido")
            return false
        }
        if (monto <= 0.0) {
            mostrarError("El monto debe ser mayor que cero")
            return false
        }
        if (monto < state.total) {
            val faltante = state.total - monto
            mostrarError("El monto es insuficiente. Faltan RD$ ${"%.2f".format(faltante)}")
            return false
        }
        if (monto > 1_000_000.0) {
            mostrarError("Verifica el monto ingresado")
            return false
        }
        return true
    }

    private fun validarTransferencia(state: PagoUiState): Boolean {
        val titular = state.titularTransferencia.trim()
        val banco = state.banco.trim()
        val referencia = state.referenciaTransferencia.trim()

        if (titular.isBlank()) {
            mostrarError("Ingresa el nombre del remitente")
            return false
        }
        if (titular.length < 3) {
            mostrarError("El nombre del remitente es demasiado corto")
            return false
        }
        if (titular.length > 80) {
            mostrarError("El nombre del remitente es demasiado largo")
            return false
        }
        if (titular.any { it.isDigit() }) {
            mostrarError("El nombre del remitente no debe contener números")
            return false
        }
        if (banco.isBlank()) {
            mostrarError("Indica el banco desde donde transferiste")
            return false
        }
        if (banco.length < 3) {
            mostrarError("Introduce un banco válido")
            return false
        }
        if (referencia.isBlank()) {
            mostrarError("Ingresa la referencia de la transferencia")
            return false
        }
        if (referencia.length < 4) {
            mostrarError("La referencia es demasiado corta")
            return false
        }
        return true
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
                        guarnicion = buildList {
                            item.guarnicion?.nombreGuarnicion?.takeIf { it.isNotBlank() }?.let { add(it) }
                            addAll(item.guarnicionesExtra.map { "${it.nombreGuarnicion} (Extra)" })
                        }.joinToString(", "),
                        idGuarnicion = item.guarnicion?.idGuarnicion,
                        salsa = buildList {
                            item.salsa?.nombreComponente?.takeIf { it.isNotBlank() }?.let { add(it) }
                            addAll(item.salsasExtra.map { "${it.nombreComponente} (Extra)" })
                        }.joinToString(", "),
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
                
                notificacionRepository.agregar(
                    Notificacion(
                        titulo = "Pedido recibido",
                        mensaje = "Tu pedido #$idPedido fue recibido correctamente.",
                        tipo = TipoNotificacion.PEDIDO,
                        destino = DestinoNotificacion.CLIENTE,
                        usuarioUid = pedido.usuarioUid,
                        idReferencia = idPedido
                    )
                )

                notificacionRepository.agregar(
                    Notificacion(
                        titulo = "Nuevo pedido",
                        mensaje = "${pedido.clienteNombre} realizó el pedido #$idPedido.",
                        tipo = TipoNotificacion.PEDIDO,
                        destino = DestinoNotificacion.ADMINISTRADOR,
                        usuarioUid = null,
                        idReferencia = idPedido
                    )
                )

                carritoRepository.vaciar()

                _uiState.update { it.copy(isLoading = false, pedidoCreadoId = idPedido) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "No se pudo crear el pedido") }
            }
        }
    }

    private fun mostrarError(mensaje: String) = _uiState.update { it.copy(errorMessage = mensaje, isLoading = false) }
}
