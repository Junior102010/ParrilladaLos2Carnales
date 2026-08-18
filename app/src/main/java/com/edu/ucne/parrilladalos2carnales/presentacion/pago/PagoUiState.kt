package com.edu.ucne.parrilladalos2carnales.presentacion.pago


enum class TipoEntrega {
    DELIVERY,
    RECOGER
}


enum class MetodoPago {
    EFECTIVO,
    TRANSFERENCIA
}


data class PagoUiState(


    val tipoEntrega:
        TipoEntrega =
        TipoEntrega.DELIVERY,


    val direccion:
        String = "",


    val metodoPago:
        MetodoPago =
        MetodoPago.EFECTIVO,


    // EFECTIVO
    val montoRecibido:
        String = "",


    // TRANSFERENCIA
    val titularTransferencia:
        String = "",


    val banco:
        String = "",


    val referenciaTransferencia:
        String = "",


    val subtotal:
        Double = 0.0,


    val delivery:
        Double = 100.0,


    val total:
        Double = 0.0,


    val isLoading:
        Boolean = false,


    val errorMessage:
        String? = null,


    val pedidoCreadoId:
        Int? = null
)
