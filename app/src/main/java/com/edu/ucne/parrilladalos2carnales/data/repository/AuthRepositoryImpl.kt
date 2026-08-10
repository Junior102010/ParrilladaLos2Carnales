package com.edu.ucne.parrilladalos2carnales.data.repository

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.edu.ucne.parrilladalos2carnales.domain.model.Registro.RegistroUsuario
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val credentialManager: CredentialManager
) : AuthRepository {

    override suspend fun login(
        username: String,
        clave: String
    ): Result<Boolean> {
        return try {
            firebaseAuth
                .signInWithEmailAndPassword(username.trim(), clave).await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registro(
        usuario: RegistroUsuario
    ): Result<Boolean> {
        return try {
            firebaseAuth
                .createUserWithEmailAndPassword(usuario.correo.trim(), usuario.contrasena).await()
            val nombreCompleto = "${usuario.nombre} ${usuario.apellido}".trim()
            val profileUpdates =
                UserProfileChangeRequest
                    .Builder()
                    .setDisplayName(nombreCompleto)
                    .build()
            firebaseAuth.currentUser
                ?.updateProfile(profileUpdates)?.await()
            firebaseAuth.currentUser?.reload()?.await()
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun signInWithGoogle(
        context: Context
    ): Result<Boolean> {

        return try {

            val googleIdOption =
                GetGoogleIdOption
                    .Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("37553611244-bsofaeuqil5tq7jiil5tc4st8c5futte.apps.googleusercontent.com")
                    .setAutoSelectEnabled(true)
                    .build()
            val request =
                GetCredentialRequest.Builder().addCredentialOption(googleIdOption).build()
            val result =
                credentialManager.getCredential(context, request)
            val credential =
                result.credential
            if (
                credential is CustomCredential &&
                credential.type ==
                GoogleIdTokenCredential
                    .TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {
                val googleCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)
                val firebaseCredential =
                    GoogleAuthProvider
                        .getCredential(
                            googleCredential.idToken,
                            null
                        )
                firebaseAuth
                    .signInWithCredential(firebaseCredential).await()
                firebaseAuth.currentUser?.reload()?.await()
                Result.success(true)
            } else {
                Result.failure(
                    Exception("Credencial de Google no válida")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUsuarioLogueado(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun getUsuarioUid(): String? {
        return firebaseAuth.currentUser?.uid
    }

    override fun getNombreUsuario(): String? {
        return firebaseAuth.currentUser?.displayName
    }

    override fun getCorreoUsuario(): String? {
        return firebaseAuth.currentUser?.email
    }

    override fun getFotoUsuario(): String? {
        return firebaseAuth.currentUser?.photoUrl?.toString()
    }

    override fun esAdministrador(): Boolean {
        return firebaseAuth.currentUser?.email.equals("admin@parrillada.com", ignoreCase = true)
    }

    override fun cerrarSesion() {
        firebaseAuth.signOut()
    }
}