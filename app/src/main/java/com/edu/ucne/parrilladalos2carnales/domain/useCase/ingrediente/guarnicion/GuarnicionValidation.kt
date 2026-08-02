package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

data class GuarnicionValidations(
    val isValid : Boolean = false,
    val error : String? = ""
)

fun validateNombreGuarnicion(nombre : String) : GuarnicionValidations
{
    return when{
        nombre.isBlank() -> GuarnicionValidations(false,"El Nombre NO debe estar Vacio")
        nombre.length <= 2 -> GuarnicionValidations(false,"El Nombre debe ser de 2 Letras o Mas")
        else -> GuarnicionValidations(true)
    }
}

fun validateDescripcionGuarnicion(descripcion : String) : GuarnicionValidations
{
    return when{
        descripcion.isBlank() -> GuarnicionValidations(false,"La Descripcion NO debe estar Vacia")
        descripcion.length <= 2 -> GuarnicionValidations(false,"La Descripcion debe ser de 2 Letras o Mas")
        else -> GuarnicionValidations(true)
    }
}

fun validateCantidadGuarnicion(cantidad : Double) : GuarnicionValidations
{
    return when{
        cantidad < 1 -> GuarnicionValidations(false,"La Cantidad NO debe ser menos de 1")
        else -> GuarnicionValidations(true)
    }
}

fun validatePrecioGuarnicion(precio : Double) : GuarnicionValidations
{
    return when{
        precio < 1 -> GuarnicionValidations(false,"El Precio NO debe ser 0")
        else -> GuarnicionValidations(true)
    }
}