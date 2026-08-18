package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato


data class PlatoValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)


fun validateNombrePlato(nombre: String): PlatoValidations {
    val valor = nombre.trim()
    return when {
        valor.isBlank() -> PlatoValidations(false, "El nombre del plato no debe estar vacío")
        valor.length <= 2 ->
            PlatoValidations(
                false,
                "El nombre debe tener más de 2 caracteres"
            )


        valor.length > 80 ->
            PlatoValidations(
                false,
                "El nombre del plato es demasiado largo"
            )


        else ->
            PlatoValidations(
                true
            )
    }
}


fun validatePrecioPlato(
    precio: Double
): PlatoValidations {


    return when {


        precio <= 0.0 ->
            PlatoValidations(
                false,
                "El precio debe ser mayor a 0"
            )


        precio > 1_000_000.0 ->
            PlatoValidations(
                false,
                "Verifica el precio ingresado"
            )


        else ->
            PlatoValidations(
                true
            )
    }
}


fun validateCategoriaPlato(
    idCategoria: Int
): PlatoValidations {


    return if (
        idCategoria <= 0
    ) {


        PlatoValidations(
            false,
            "Selecciona una categoría"
        )


    } else {


        PlatoValidations(
            true
        )
    }
}


fun validateDescripcionPlato(
    descripcion: String
): PlatoValidations {


    return if (
        descripcion.length > 300
    ) {


        PlatoValidations(
            false,
            "La descripción no puede superar los 300 caracteres"
        )


    } else {


        PlatoValidations(
            true
        )
    }
}
