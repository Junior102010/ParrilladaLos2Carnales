package com.edu.ucne.parrilladalos2carnales.presentacion.pago

enum class TipoEntrega {
    DELIVERY,
    RECOGER
}

enum class MetodoPago {
    EFECTIVO,
    TARJETA,
    TRANSFERENCIA
}

data class PagoUiState(
    val tipoEntrega: TipoEntrega = TipoEntrega.DELIVERY,
    val direccion: String = "Dirección actual",
    val metodoPago: MetodoPago = MetodoPago.TARJETA,
    val numeroTarjeta: String = "",
    val fechaTarjeta: String = "",
    val cvv: String = "",
    val montoRecibido: String = "",
    val titularTransferencia: String = "",
    val cuentaOrigen: String = "",
    val banco: String = "",
    val subtotal: Double = 0.0,
    val delivery: Double = 100.0,
    val total: Double = 0.0,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val pedidoCreadoId: Int? = null
)
