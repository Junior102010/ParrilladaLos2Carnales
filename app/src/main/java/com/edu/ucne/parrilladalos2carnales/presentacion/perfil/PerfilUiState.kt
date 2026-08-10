package com.edu.ucne.parrilladalos2carnales.presentacion.perfil

data class PerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val fotoUrl: String? = null,
    val sesionCerrada: Boolean = false
)
