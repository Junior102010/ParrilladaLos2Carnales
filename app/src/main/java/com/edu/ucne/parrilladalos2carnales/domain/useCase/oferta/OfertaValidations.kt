package com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta

data class OfertaValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateTituloOferta(titulo: String): OfertaValidations {
    val valor = titulo.trim()
    return when {
        valor.isBlank() -> OfertaValidations(false, "El título es obligatorio")
        valor.length < 3 -> OfertaValidations(false, "El título debe tener al menos 3 caracteres")
        else -> OfertaValidations(true)
    }
}

fun validateDescripcionOferta(descripcion: String): OfertaValidations {
    return when {
        descripcion.length > 250 ->
            OfertaValidations(
                false,
                "La descripción no puede superar los 250 caracteres"
            )


        else ->
            OfertaValidations(
                true
            )
    }
}


fun validatePlatoOferta(
    idPlato: Int?
): OfertaValidations {


    return if (
        idPlato == null ||
        idPlato <= 0
    ) {


        OfertaValidations(
            false,
            "Selecciona el plato de la oferta"
        )


    } else {


        OfertaValidations(
            true
        )
    }
}


fun validateDescuentoOferta(
    descuento: Double
): OfertaValidations {
    return when {
        descuento <= 0 ->
            OfertaValidations(
                false,
                "El descuento debe ser mayor que 0%"
            )

        descuento > 100 ->
            OfertaValidations(
                false,
                "El descuento no puede superar el 100%"
            )

        else ->
            OfertaValidations(
                true
            )
    }
}

fun validateDescuentoOferta(
    descuentoTexto: String
): OfertaValidations {
    val descuento =
        descuentoTexto.toDoubleOrNull()

    return when {
        descuento == null ->
            OfertaValidations(
                false,
                "Introduce un descuento válido"
            )

        else -> validateDescuentoOferta(descuento)
    }
}
