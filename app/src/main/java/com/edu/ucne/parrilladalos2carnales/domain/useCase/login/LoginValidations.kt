package com.edu.ucne.parrilladalos2carnales.domain.useCase.login


data class LoginValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)


fun validateLoginCorreo(
    correo: String
): LoginValidations {


    val correoLimpio =
        correo.trim()


    return when {


        correoLimpio.isBlank() ->
            LoginValidations(
                false,
                "El correo es obligatorio"
            )


        correoLimpio.length > 100 ->
            LoginValidations(
                false,
                "El correo es demasiado largo"
            )


        !correoLimpio.matches(
            Regex(
                "^[A-Za-z0-9+_.-]+@" +
                    "[A-Za-z0-9.-]+\\." +
                    "[A-Za-z]{2,}$"
            )
        ) ->
            LoginValidations(
                false,
                "Introduce un correo electrónico válido"
            )


        else ->
            LoginValidations(
                true
            )
    }
}


fun validateLoginPassword(
    password: String
): LoginValidations {


    return when {


        password.isBlank() ->
            LoginValidations(
                false,
                "La contraseña es obligatoria"
            )


        password.length < 6 ->
            LoginValidations(
                false,
                "La contraseña debe tener al menos 6 caracteres"
            )


        else ->
            LoginValidations(
                true
            )
    }
}
