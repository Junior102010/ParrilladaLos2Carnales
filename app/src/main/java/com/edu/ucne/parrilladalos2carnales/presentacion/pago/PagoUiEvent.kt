package com.edu.ucne.parrilladalos2carnales.presentacion.pago


sealed interface PagoUiEvent {


    data class OnTipoEntregaChange(
        val tipo: TipoEntrega
    ) : PagoUiEvent


    data class OnDireccionChange(
        val direccion: String
    ) : PagoUiEvent


    data class OnMetodoPagoChange(
        val metodo: MetodoPago
    ) : PagoUiEvent


    data class OnMontoRecibidoChange(
        val monto: String
    ) : PagoUiEvent


    data class OnTitularTransferenciaChange(
        val titular: String
    ) : PagoUiEvent


    data class OnBancoChange(
        val banco: String
    ) : PagoUiEvent


    data class OnReferenciaTransferenciaChange(
        val referencia: String
    ) : PagoUiEvent


    data object OnConfirmarPago :
        PagoUiEvent


    data object OnPedidoCreadoConsumido :
        PagoUiEvent
}
