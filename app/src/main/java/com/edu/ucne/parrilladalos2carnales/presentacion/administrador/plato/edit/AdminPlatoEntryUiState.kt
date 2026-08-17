package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit


import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria

data class AdminPlatoEntryUiState(
    val idPlato: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: String = "",
    val idCategoria: Int = 0,
    val disponible: Boolean = true,
    val imagenUrl: String = "",
    val categorias: List<Categoria> = emptyList(),
    val nombreError: String? = null,
    val precioError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)