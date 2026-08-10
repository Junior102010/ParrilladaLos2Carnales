package com.edu.ucne.parrilladalos2carnales.domain.repository.login

import android.content.Context
import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario

interface AuthRepository {

    suspend fun login(
        username: String,
        clave: String
    ): Result<Boolean>

    suspend fun registro(
        usuario: RegistroUsuario
    ): Result<Boolean>

    suspend fun signInWithGoogle(
        context: Context
    ): Result<Boolean>

    fun isUsuarioLogueado(): Boolean

    fun getUsuarioUid(): String?

    fun getNombreUsuario(): String?

    fun getCorreoUsuario(): String?

    fun getFotoUsuario(): String?

    fun esAdministrador(): Boolean

    fun cerrarSesion()
}

