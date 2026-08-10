package com.edu.ucne.parrilladalos2carnales.data.plato.remote

data class PlatoDto(
    val idPlato: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val precio: Double = 0.0,
    val idCategoria: Int = 0,
    val imagenUrl: String = ""
)
