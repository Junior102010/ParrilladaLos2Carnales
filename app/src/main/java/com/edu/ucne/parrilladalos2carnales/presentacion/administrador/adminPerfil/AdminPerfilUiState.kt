package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminPerfil

data class AdminPerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val fotoUrl: String? = null,
    val isLoading: Boolean = false
)
