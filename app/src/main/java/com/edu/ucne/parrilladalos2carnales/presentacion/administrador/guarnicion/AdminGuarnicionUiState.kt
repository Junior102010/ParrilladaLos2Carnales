package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion

data class AdminGuarnicionUiState(
    val idGuarnicion: Int = 0,
    val nombreGuarnicion: String = "",
    val descripcionGuarnicion: String = "",
    val cantidadGuarnicion: Double = 0.0,
    val precioGuarnicion: String = "",
    val categoria: String = "",
    val disponible: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)