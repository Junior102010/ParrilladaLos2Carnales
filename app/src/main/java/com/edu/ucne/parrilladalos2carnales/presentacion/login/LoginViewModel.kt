package com.edu.ucne.parrilladalos2carnales.presentacion.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.login.validateLoginCorreo
import com.edu.ucne.parrilladalos2carnales.domain.useCase.login.validateLoginPassword
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {


    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var isPasswordVisible by mutableStateOf(false)

    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)


    fun onTogglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun onLoginClick(onSuccess: (Rol) -> Unit) {
        if (isLoading) {
            return
        }

        val correoValidation = validateLoginCorreo(username)

        if (!correoValidation.isValid) {
            errorMessage = correoValidation.errorMessage
            return
        }

        val passwordValidation = validateLoginPassword(password)

        if (!passwordValidation.isValid) {
            errorMessage = passwordValidation.errorMessage
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null


            try {
                val result = authRepository.login(
                    username = username.trim(),
                    clave = password
                )


                if (result.isSuccess) {
                    onSuccess(currentUserRole())
                } else {
                    errorMessage = mensajeErrorLogin(result.exceptionOrNull())
                }
            } finally {
                isLoading = false
            }
        }
    }

    fun loginConGoogle(
        context: Context,
        onSuccess: (Rol) -> Unit
    ) {
        if (isLoading) {
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val result = authRepository.signInWithGoogle(context)

                if (result.isSuccess) {
                    onSuccess(currentUserRole())
                } else {
                    errorMessage = mensajeErrorLogin(result.exceptionOrNull())
                }
            } finally {
                isLoading = false
            }
        }
    }

    private fun mensajeErrorLogin(
        error: Throwable?
    ): String {

        return when (
            error
        ) {

            is FirebaseAuthInvalidCredentialsException ->
                "Correo o contraseña incorrectos"

            is FirebaseAuthInvalidUserException ->
                "Esta cuenta no existe o está deshabilitada"

            is FirebaseNetworkException ->
                "No se pudo conectar con Firebase. Revisa tu conexión a internet"

            is FirebaseTooManyRequestsException ->
                "Demasiados intentos. Espera un momento e inténtalo nuevamente"

            null ->
                "No se pudo iniciar sesión"

            else ->
                error.localizedMessage
                    ?.takeIf {
                        it.isNotBlank()
                    }
                    ?: "No se pudo iniciar sesión"
        }
    }

    private fun currentUserRole(): Rol {
        return if (authRepository.esAdministrador()) {
            Rol.ADMINISTRADOR
        } else {
            Rol.CLIENTE
        }
    }

}
