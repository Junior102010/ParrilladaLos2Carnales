package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

data class GuarnicionValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateNombreGuarnicion(nombre: String): GuarnicionValidations {
    return if (nombre.isBlank()) {
        GuarnicionValidations(false, "El nombre no puede estar vacío")
    } else {
        GuarnicionValidations(true)
    }
}

fun validateDescripcionGuarnicion(descripcion: String): GuarnicionValidations {
    return if (descripcion.isBlank()) {
        GuarnicionValidations(false, "La descripción no puede estar vacía")
    } else {
        GuarnicionValidations(true)
    }
}

fun validatePrecioGuarnicion(precioTexto: String): GuarnicionValidations {
    val precio = precioTexto.toDoubleOrNull()
    return when {
        precio == null -> GuarnicionValidations(false, "Introduce un precio válido")
        else -> validatePrecioGuarnicion(precio)
    }
}

fun validatePrecioGuarnicion(precio: Double): GuarnicionValidations {
    return if (precio < 0.0) {
        GuarnicionValidations(false, "El precio adicional no puede ser negativo")
    } else {
        GuarnicionValidations(true)
    }
}

fun validateCantidadGuarnicion(cantidad: Double): GuarnicionValidations {
    return if (cantidad < 0) {
        GuarnicionValidations(false, "La cantidad no puede ser negativa")
    } else {
        GuarnicionValidations(true)
    }
}

fun validateCategoriaGuarnicion(categoria: String): GuarnicionValidations {
    return if (categoria.isBlank()) {
        GuarnicionValidations(false, "Selecciona una categoría")
    } else {
        GuarnicionValidations(true)
    }
}
