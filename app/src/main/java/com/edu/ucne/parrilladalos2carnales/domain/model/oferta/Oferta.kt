package com.edu.ucne.parrilladalos2carnales.domain.model.oferta

data class Oferta(
    val idOferta: Int = 0,
    val tituloOferta: String = "",
    val descripcionOferta: String = "",
    val descuento: Double = 0.0,
    val imagenUrl: String = "",
    val descuentoPorcentaje: Double = 0.0,
    val titulo: String = ""
)