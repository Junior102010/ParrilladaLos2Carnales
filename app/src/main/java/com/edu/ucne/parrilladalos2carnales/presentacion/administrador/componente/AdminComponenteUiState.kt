package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente

data class AdminComponenteUiState(
    val idComponente: Int = 0,
    val nombreComponente: String = "",
    val descripcionComponente: String = "",
    val cantidadComponente: Double = 0.0,
    val precioComponente: String = "",
    val categoriaComponente: String = "Salsa",
    val coccion: String = "",
    val disponible: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val guardadoExitoso: Boolean = false
)
