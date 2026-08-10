package com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente

data class Componente(

    val idComponente : Int,
    val nombreComponente: String,
    val descripcionComponente: String,
    val cantidadComponente: Double,
    val precioComponente: Double,
    val disponible : Boolean,
    val coccion: String?,
    val categoriaComponente: String,

)
