package com.edu.ucne.parrilladalos2carnales.domain.useCase.perfil


data class PerfilValidations(
    val isValid: Boolean = false,
    val errorMessage: String? = null
)


fun validateNombrePerfil(
    nombre: String
): PerfilValidations {


    val valor =
        nombre.trim()


    return when {


        valor.isBlank() ->
            PerfilValidations(
                false,
                "El nombre es obligatorio"
            )


        valor.length < 2 ->
            PerfilValidations(
                false,
                "El nombre debe tener al menos 2 caracteres"
            )


        valor.length > 60 ->
            PerfilValidations(
                false,
                "El nombre no puede superar los 60 caracteres"
            )


        valor.none {
            it.isLetter()
        } ->
            PerfilValidations(
                false,
                "Introduce un nombre válido"
            )


        else ->
            PerfilValidations(
                true
            )
    }
}
