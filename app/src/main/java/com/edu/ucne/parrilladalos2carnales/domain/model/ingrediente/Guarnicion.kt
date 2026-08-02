package com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente

data class Guarnicion(

    val idGuarnicion : Int,
    val nombreGuarnicion: String,
    val descripcionGuarnicion: String,
    val cantidadGuarnicion: Double,
    val precioGuarnicion: Double,
    val disponible : Boolean,
    val categoria: String,

    )