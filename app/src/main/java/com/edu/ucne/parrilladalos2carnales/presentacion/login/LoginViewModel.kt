package com.edu.ucne.parrilladalos2carnales.presentacion.login

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
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


        if (username.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, llena todos los campos"
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
                    errorMessage = "Usuario o contraseña incorrectos"
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
                    errorMessage =
                        result.exceptionOrNull()?.localizedMessage
                            ?: "No se pudo iniciar sesión con Google"
                }
            } finally {
                isLoading = false
            }
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
