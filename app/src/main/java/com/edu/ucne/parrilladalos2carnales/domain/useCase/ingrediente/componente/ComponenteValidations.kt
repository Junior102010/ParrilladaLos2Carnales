package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

data class ComponenteValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateNombreComponente(nombre: String): ComponenteValidations {
    val valor = nombre.trim()
    return if (valor.length < 2) {
        ComponenteValidations(false, "Introduce un nombre válido")
    } else {
        ComponenteValidations(true)
    }
}

fun validateCategoriaComponente(categoria: String): ComponenteValidations {
    return if (categoria.isBlank()) {
        ComponenteValidations(false, "Selecciona una categoría")
    } else {
        ComponenteValidations(true)
    }
}

fun validateDescripcionComponente(descripcion: String): ComponenteValidations {
    return if (descripcion.isBlank()) {
        ComponenteValidations(false, "La descripción no puede estar vacía")
    } else {
        ComponenteValidations(true)
    }
}

fun validateCantidadComponente(cantidad: Double): ComponenteValidations {
    return if (cantidad < 0) {
        ComponenteValidations(false, "La cantidad no puede ser negativa")
    } else {
        ComponenteValidations(true)
    }
}

fun validatePrecioComponente(
    precioTexto: String,
    categoria: String
): ComponenteValidations {


    val precio =
        precioTexto.toDoubleOrNull()


    if (precio == null) {


        return ComponenteValidations(
            false,
            "Introduce un precio válido"
        )
    }


    return validatePrecioComponente(precio, categoria)
}

fun validatePrecioComponente(
    precio: Double,
    categoria: String
): ComponenteValidations {
    if (precio < 0.0) {
        return ComponenteValidations(
            false,
            "El precio no puede ser negativo"
        )
    }


    if (
        categoria.equals(
            "Coccion",
            ignoreCase = true
        ) &&
        precio != 0.0
    ) {


        return ComponenteValidations(
            false,
            "El término de cocción no debe tener precio"
        )
    }


    return ComponenteValidations(
        true
    )
}


fun validateCoccion(
    categoria: String,
    coccion: String
): ComponenteValidations {


    if (
        !categoria.equals(
            "Coccion",
            ignoreCase = true
        )
    ) {


        return ComponenteValidations(
            true
        )
    }


    return if (
        coccion.isBlank()
    ) {


        ComponenteValidations(
            false,
            "Indica el término de cocción"
        )


    } else {


        ComponenteValidations(
            true
        )
    }
}
