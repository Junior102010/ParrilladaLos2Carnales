package com.edu.ucne.parrilladalos2carnales.presentacion.perfil.editar

data class EditarPerfilUiState(
    val nombre: String = "",
    val correo: String = "",
    val fotoUrl: String? = null,
    val rol: String = "",
    val isLoading: Boolean = false,
    val isProcessingImage: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)
