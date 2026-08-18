package com.edu.ucne.parrilladalos2carnales.domain.useCase.pago

data class PagoValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateDireccionPago(direccion: String): PagoValidations {
    val valor = direccion.trim()
    return when {
        valor.isBlank() -> PagoValidations(false, "Debes indicar la dirección de entrega")
        valor.length < 8 -> PagoValidations(false, "Escribe una dirección más completa")
        valor.length > 150 -> PagoValidations(false, "La dirección es demasiado larga")
        else -> PagoValidations(true)
    }
}

fun validateMontoEfectivo(montoRecibido: String, total: Double): PagoValidations {
    if (montoRecibido.isBlank()) {
        return PagoValidations(false, "Indica con cuánto efectivo pagarás")
    }
    val monto = montoRecibido.toDoubleOrNull()
    return when {
        monto == null -> PagoValidations(false, "Introduce un monto válido")
        monto <= 0.0 -> PagoValidations(false, "El monto debe ser mayor que cero")
        monto < total -> {
            val faltante = total - monto
            PagoValidations(false, "El monto es insuficiente. Faltan RD$ ${"%.2f".format(faltante)}")
        }
        monto > 1_000_000.0 -> PagoValidations(false, "Verifica el monto ingresado")
        else -> PagoValidations(true)
    }
}

fun validateTitularTransferencia(titular: String): PagoValidations {
    val valor = titular.trim()
    return when {
        valor.isBlank() -> PagoValidations(false, "Ingresa el nombre del remitente")
        valor.length < 3 -> PagoValidations(false, "El nombre del remitente es demasiado corto")
        valor.length > 80 -> PagoValidations(false, "El nombre del remitente es demasiado largo")
        valor.any { it.isDigit() } -> PagoValidations(false, "El nombre del remitente no debe contener números")
        else -> PagoValidations(true)
    }
}

fun validateBancoTransferencia(banco: String): PagoValidations {
    val valor = banco.trim()
    return when {
        valor.isBlank() -> PagoValidations(false, "Indica el banco desde donde transferiste")
        valor.length < 3 -> PagoValidations(false, "Introduce un banco válido")
        valor.length > 60 -> PagoValidations(false, "El nombre del banco es demasiado largo")
        else -> PagoValidations(true)
    }
}

fun validateReferenciaTransferencia(referencia: String): PagoValidations {
    val valor = referencia.trim()
    return when {
        valor.isBlank() -> PagoValidations(false, "Ingresa la referencia de la transferencia")
        valor.length < 4 -> PagoValidations(false, "La referencia es demasiado corta")
        valor.length > 30 -> PagoValidations(false, "La referencia es demasiado larga")
        !valor.all { it.isLetterOrDigit() || it == '-' || it == '_' } ->
            PagoValidations(false, "La referencia contiene caracteres no válidos")
        else -> PagoValidations(true)
    }
}
