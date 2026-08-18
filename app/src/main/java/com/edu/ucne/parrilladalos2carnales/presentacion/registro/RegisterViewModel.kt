package com.edu.ucne.parrilladalos2carnales.presentacion.registro

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.registro.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    var pasoActual by mutableIntStateOf(1)

    var nombreUsuario by mutableStateOf("")
    var correo by mutableStateOf("")
    var contrasena by mutableStateOf("")
    var confirmarContrasena by mutableStateOf("")
    var contrasenaVisible by mutableStateOf(false)

    var nombre by mutableStateOf("")
    var apellido by mutableStateOf("")
    var telefono by mutableStateOf("")

    var calle by mutableStateOf("")
    var numero by mutableStateOf("")
    var ciudad by mutableStateOf("")
    var codigoPostal by mutableStateOf("")
    var referencia by mutableStateOf("")

    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    fun alternarVisibilidadContrasena() {
        contrasenaVisible = !contrasenaVisible
    }

    fun siguientePaso() {
        mensajeError = null
        when (pasoActual) {
            1 -> {
                val correoValidation = validateRegistroCorreo(correo)
                if (!correoValidation.isValid) {
                    mensajeError = correoValidation.errorMessage
                    return
                }

                val passwordValidation = validateRegistroPassword(contrasena)
                if (!passwordValidation.isValid) {
                    mensajeError = passwordValidation.errorMessage
                    return
                }

                val confirmarValidation = validateConfirmarPassword(contrasena, confirmarContrasena)
                if (!confirmarValidation.isValid) {
                    mensajeError = confirmarValidation.errorMessage
                    return
                }

                pasoActual = 2
            }
            2 -> {
                val nombreValidation = validateNombrePersona(nombre, "nombre")
                if (!nombreValidation.isValid) {
                    mensajeError = nombreValidation.errorMessage
                    return
                }

                val apellidoValidation = validateNombrePersona(apellido, "apellido")
                if (!apellidoValidation.isValid) {
                    mensajeError = apellidoValidation.errorMessage
                    return
                }

                val telefonoValidation = validateTelefono(telefono)
                if (!telefonoValidation.isValid) {
                    mensajeError = telefonoValidation.errorMessage
                    return
                }

                pasoActual = 3
            }
        }
    }

    fun pasoAnterior() {
        mensajeError = null
        if (pasoActual > 1) {
            pasoActual -= 1
        }
    }

    fun registrarse(onSuccess: (Rol) -> Unit) {
        val calleValidation = validateCalle(calle)
        if (!calleValidation.isValid) {
            mensajeError = calleValidation.errorMessage
            return
        }

        val numeroValidation = validateNumeroVivienda(numero)
        if (!numeroValidation.isValid) {
            mensajeError = numeroValidation.errorMessage
            return
        }

        val ciudadValidation = validateCiudad(ciudad)
        if (!ciudadValidation.isValid) {
            mensajeError = ciudadValidation.errorMessage
            return
        }

        val postalValidation = validateCodigoPostal(codigoPostal)
        if (!postalValidation.isValid) {
            mensajeError = postalValidation.errorMessage
            return
        }

        viewModelScope.launch {
            estaCargando = true
            mensajeError = null

            val usuario = RegistroUsuario(
                nombreUsuario = nombreUsuario.ifBlank {
                    correo.trim().substringBefore("@")
                },
                correo = correo.trim(),
                contrasena = contrasena,
                nombre = nombre.trim(),
                apellido = apellido.trim(),
                telefono = telefono.trim(),
                calle = calle.trim(),
                numero = numero.trim(),
                ciudad = ciudad.trim(),
                codigoPostal = codigoPostal.trim(),
                referencia = referencia.trim()
            )

            try {
                val resultado = authRepository.registro(usuario)

                if (resultado.isSuccess) {
                    onSuccess(rolUsuarioActual())
                } else {
                    mensajeError = resultado.exceptionOrNull()?.localizedMessage
                        ?: "Error al registrar. Inténtalo de nuevo."
                }
            } finally {
                estaCargando = false
            }
        }
    }


    private fun rolUsuarioActual(): Rol {
        return if (authRepository.esAdministrador()) {
            Rol.ADMINISTRADOR
        } else {
            Rol.CLIENTE
        }
    }
}
