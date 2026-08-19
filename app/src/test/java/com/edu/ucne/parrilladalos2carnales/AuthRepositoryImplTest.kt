package com.edu.ucne.parrilladalos2carnales

import android.content.Context
import androidx.credentials.CredentialManager
import com.edu.ucne.parrilladalos2carnales.data.repository.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class AuthRepositoryImplTest {

    private lateinit var repository: AuthRepositoryImpl
    private val firebaseAuth: FirebaseAuth = mockk()
    private val credentialManager: CredentialManager = mockk()
    private val context: Context = mockk()
    private val firebaseUser: FirebaseUser = mockk()

    @Before
    fun setup() {
        repository = AuthRepositoryImpl(firebaseAuth, credentialManager, context)
    }

    @Test
    fun `esAdministrador retorna true cuando el correo es admin@parrillada com`() {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "admin@parrillada.com"

        val result = repository.esAdministrador()

        assertTrue(result)
    }

    @Test
    fun `esAdministrador retorna false cuando el correo no es de admin`() {
        every { firebaseAuth.currentUser } returns firebaseUser
        every { firebaseUser.email } returns "user@correo.com"

        val result = repository.esAdministrador()

        assertFalse(result)
    }
}
