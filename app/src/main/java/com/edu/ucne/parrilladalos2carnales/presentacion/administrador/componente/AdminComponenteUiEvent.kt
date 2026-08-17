package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente

sealed interface AdminComponenteUiEvent {
    data class OnNombreChange(val nombre: String) : AdminComponenteUiEvent
    data class OnDescripcionChange(val descripcion: String) : AdminComponenteUiEvent
    data class OnCantidadChange(val cantidad: Double) : AdminComponenteUiEvent
    data class OnPrecioChange(val precio: String) : AdminComponenteUiEvent
    data class OnCategoriaChange(val categoria: String) : AdminComponenteUiEvent
    data class OnCoccionChange(val coccion: String) : AdminComponenteUiEvent
    data class OnDisponibleChange(val disponible: Boolean) : AdminComponenteUiEvent
    data object OnGuardarClick : AdminComponenteUiEvent
    data object OnBackClick : AdminComponenteUiEvent
    data object ResetSuccess : AdminComponenteUiEvent
}
