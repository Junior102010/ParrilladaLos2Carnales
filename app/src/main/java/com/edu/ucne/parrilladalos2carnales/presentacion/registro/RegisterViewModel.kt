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
                if (correo.isBlank()) {
                    mensajeError = "El correo es obligatorio"
                    return
                }
                if (!correoEsValido(correo)) {
                    mensajeError = "Ingresa un correo electrónico válido"
                    return
                }
                if (contrasena.length < 6) {
                    mensajeError =
                        "La contraseña debe tener al menos 6 caracteres"
                    return
                }


                if (contrasena != confirmarContrasena) {
                    mensajeError = "Las contraseñas no coinciden"
                    return
                }
                pasoActual = 2
            }
            2 -> {
                if (nombre.isBlank() || apellido.isBlank() || telefono.isBlank()) {
                    mensajeError = "Por favor, completa tus datos personales"
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
        if (calle.isBlank() || numero.isBlank() || ciudad.isBlank()) {
            mensajeError = "Por favor, completa los datos de dirección"
            return
        }


        viewModelScope.launch {
            estaCargando = true
            mensajeError = null


            val usuario = RegistroUsuario(
                nombreUsuario = nombreUsuario.ifBlank {
                    correo
                        .trim()
                        .substringBefore("@")
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
                val resultado =
                    authRepository.registro(usuario)


                if (resultado.isSuccess) {
                    onSuccess(rolUsuarioActual())
                } else {
                    mensajeError =
                        resultado
                            .exceptionOrNull()
                            ?.localizedMessage
                            ?: "Error al registrar. Inténtalo de nuevo."
                }
            } finally {
                estaCargando = false
            }
        }
    }


    private fun correoEsValido(
        correo: String
    ): Boolean {
        return correo
            .trim()
            .matches(
                Regex(
                    "^[A-Za-z0-9+_.-]+@" +
                        "[A-Za-z0-9.-]+\\." +
                        "[A-Za-z]{2,}$"
                )
            )
    }


    private fun rolUsuarioActual(): Rol {
        return if (authRepository.esAdministrador()) {
            Rol.ADMINISTRADOR
        } else {
            Rol.CLIENTE
        }
    }
}
