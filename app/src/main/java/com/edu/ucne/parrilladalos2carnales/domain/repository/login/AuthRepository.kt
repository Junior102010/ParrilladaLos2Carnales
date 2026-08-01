package com.edu.ucne.parrilladalos2carnales.domain.repository.login

import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario

interface AuthRepository {

    suspend fun login(username: String, password: String): Result<Boolean>

    suspend fun registro(usuario: RegistroUsuario): Result<Boolean>

}