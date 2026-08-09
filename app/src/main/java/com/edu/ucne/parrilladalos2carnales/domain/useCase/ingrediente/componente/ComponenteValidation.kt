package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

data class ComponenteValidations(
    val isValid : Boolean = false,
    val error : String? = ""
)

fun validateNombreComponente(nombre : String) : ComponenteValidations
{
    return when{
        nombre.isBlank() -> ComponenteValidations(false,"El Nombre NO debe estar Vacio")
        nombre.length <= 2 -> ComponenteValidations(false,"El Nombre debe ser de 2 Letras o Mas")
        else -> ComponenteValidations(true)
    }
}

fun validateDescripcionComponente(descripcion : String) : ComponenteValidations
{
    return if (descripcion.isNotBlank() && descripcion.length <= 2) {
        ComponenteValidations(false, "La Descripcion debe ser de 2 Letras o Mas")
    } else {
        ComponenteValidations(true)
    }
}

fun validateCantidadComponente(cantidad : Double) : ComponenteValidations
{
    return when{
        cantidad < 0 -> ComponenteValidations(false,"La Cantidad NO puede ser negativa")
        else -> ComponenteValidations(true)
    }
}

fun validatePrecioComponente(precio : Double) : ComponenteValidations
{
    return when{
        precio < 0 -> ComponenteValidations(false,"El Precio NO puede ser negativo")
        else -> ComponenteValidations(true)
    }
}