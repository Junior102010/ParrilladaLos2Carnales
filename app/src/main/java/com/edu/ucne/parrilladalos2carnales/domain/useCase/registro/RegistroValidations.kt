package com.edu.ucne.parrilladalos2carnales.domain.useCase.registro

data class RegistroValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)

fun validateRegistroCorreo(correo: String): RegistroValidations {
    val valor = correo.trim()
    return when {
        valor.isBlank() -> RegistroValidations(false, "El correo es obligatorio")
        !valor.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) ->
            RegistroValidations(false, "Ingresa un correo electrónico válido")
        else -> RegistroValidations(true)
    }
}

fun validateRegistroPassword(password: String): RegistroValidations {
    return if (password.length < 6) {
        RegistroValidations(false, "La contraseña debe tener al menos 6 caracteres")
    } else {
        RegistroValidations(true)
    }
}

fun validateConfirmarPassword(password: String, confirm: String): RegistroValidations {
    return if (password != confirm) {
        RegistroValidations(false, "Las contraseñas no coinciden")
    } else {
        RegistroValidations(true)
    }
}

fun validateNombrePersona(valor: String, campo: String): RegistroValidations {
    val texto = valor.trim()
    return when {
        texto.length < 2 -> RegistroValidations(false, "Introduce un $campo válido")
        texto.any { it.isDigit() } -> RegistroValidations(false, "El $campo no debe contener números")
        else -> RegistroValidations(true)
    }
}

fun validateTelefono(telefono: String): RegistroValidations {
    val limpio = telefono.filter { it.isDigit() }
    return if (limpio.length !in 10..15) {
        RegistroValidations(false, "Introduce un número de teléfono válido")
    } else {
        RegistroValidations(true)
    }
}

fun validateCalle(calle: String): RegistroValidations {
    return if (calle.trim().length < 3) {
        RegistroValidations(false, "Introduce una calle válida")
    } else {
        RegistroValidations(true)
    }
}

fun validateNumeroVivienda(numero: String): RegistroValidations {
    return if (numero.trim().isBlank()) {
        RegistroValidations(false, "El número de la vivienda es obligatorio")
    } else {
        RegistroValidations(true)
    }
}

fun validateCiudad(ciudad: String): RegistroValidations {
    return when {
        ciudad.isBlank() -> RegistroValidations(false, "La ciudad es obligatoria")
        ciudad.trim().length < 2 -> RegistroValidations(false, "Introduce una ciudad válida")
        else -> RegistroValidations(true)
    }
}

fun validateCodigoPostal(codigoPostal: String): RegistroValidations {
    if (codigoPostal.isBlank()) {
        return RegistroValidations(true)
    }
    return when {
        codigoPostal.any { !it.isDigit() } ->
            RegistroValidations(false, "El código postal solo debe contener números")
        codigoPostal.length > 10 ->
            RegistroValidations(false, "El código postal no es válido")
        else -> RegistroValidations(true)
    }
}
