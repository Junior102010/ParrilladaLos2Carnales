package com.edu.ucne.parrilladalos2carnales.presentacion.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario
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

    var estaCargando by mutableStateOf(false)
    var mensajeError by mutableStateOf<String?>(null)

    fun alternarVisibilidadContrasena() {
        contrasenaVisible = !contrasenaVisible
    }

    fun siguientePaso() {
        mensajeError = null
        when (pasoActual) {
            1 -> {
                if (nombreUsuario.isBlank() || correo.isBlank() || contrasena.isBlank() || confirmarContrasena.isBlank()) {
                    mensajeError = "Por favor, llena todos los campos"
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
                    mensajeError = "Por favor, llena tus datos personales"
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

    fun registrarse(onSuccess: () -> Unit) {
        if (calle.isBlank() || numero.isBlank() || ciudad.isBlank() || codigoPostal.isBlank()) {
            mensajeError = "Por favor, llena los datos de dirección"
            return
        }

        viewModelScope.launch {
            estaCargando = true
            mensajeError = null

            val usuario = RegistroUsuario(
                nombreUsuario = nombreUsuario,
                correo = correo,
                contrasena = contrasena,
                nombre = nombre,
                apellido = apellido,
                telefono = telefono,
                calle = calle,
                numero = numero,
                ciudad = ciudad,
                codigoPostal = codigoPostal
            )

            val resultado = authRepository.registro(usuario)

            if (resultado.isSuccess) {
                onSuccess()
            } else {
                mensajeError = "Error al registrar. Inténtalo de nuevo."
            }

            estaCargando = false
        }
    }
}