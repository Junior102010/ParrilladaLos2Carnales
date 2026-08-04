package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit

data class AdminPlatoEntryUiState(
    val idPlato: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val imagenUrl: String = "",
    val nombreError: String? = null,
    val precioError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)