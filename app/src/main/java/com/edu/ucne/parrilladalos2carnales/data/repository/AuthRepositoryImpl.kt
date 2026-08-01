package com.edu.ucne.parrilladalos2carnales.data.repository

import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(

) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<Boolean> {

        delay(1500)


        return if (password == "123456") {
            Result.success(true)
        } else {
            Result.failure(Exception("Invalid credentials"))
        }
    }

    override suspend fun registro(usuario: RegistroUsuario): Result<Boolean> {
        delay(1500)
        return Result.success(true)
    }
}