package com.edu.ucne.parrilladalos2carnales.domain.model.plato

data class Plato(
    val idPlato: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val imagenUrl: String = "",
    val idCategoria: Int = 0,
    val disponible: Boolean = true
)