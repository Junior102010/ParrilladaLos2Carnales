package com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta

data class OfertaValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateTituloOferta(titulo: String): OfertaValidations {
    return when {
        titulo.isBlank() -> OfertaValidations(false, "El título de la oferta no debe estar vacío")
        titulo.length <= 2 -> OfertaValidations(false, "El título debe tener más de 2 caracteres")
        else -> OfertaValidations(true)
    }
}

fun validateDescuentoOferta(porcentaje: Double): OfertaValidations {
    return when {
        porcentaje <= 0.0 -> OfertaValidations(false, "El descuento debe ser mayor a 0")
        porcentaje > 100.0 -> OfertaValidations(false, "El descuento no puede ser mayor al 100%")
        else -> OfertaValidations(true)
    }
}
