package com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria

data class CategoriaValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateNombreCategoria(nombre: String): CategoriaValidations {
    return when {
        nombre.isBlank() -> CategoriaValidations(false, "El nombre de la categoría no debe estar vacío")
        nombre.length <= 2 -> CategoriaValidations(false, "El nombre debe tener más de 2 caracteres")
        else -> CategoriaValidations(true)
    }
}
