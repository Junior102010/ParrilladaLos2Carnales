package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

data class PlatoValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateNombrePlato(nombre: String): PlatoValidations {
    return when {
        nombre.isBlank() -> PlatoValidations(false, "El nombre del plato no debe estar vacío")
        nombre.length <= 2 -> PlatoValidations(false, "El nombre debe tener más de 2 caracteres")
        else -> PlatoValidations(true)
    }
}

fun validatePrecioPlato(precio: Double): PlatoValidations {
    return when {
        precio <= 0.0 -> PlatoValidations(false, "El precio debe ser mayor a 0")
        else -> PlatoValidations(true)
    }
}
