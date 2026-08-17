package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.edit


sealed interface AdminPlatoEntryUiEvent {
    data class OnNombreChanged(val nombre: String) : AdminPlatoEntryUiEvent
    data class OnDescripcionChanged(val descripcion: String) : AdminPlatoEntryUiEvent
    data class OnPrecioChanged(val precio: String) : AdminPlatoEntryUiEvent
    data class OnCategoriaChanged(val idCategoria: Int) : AdminPlatoEntryUiEvent
    data class OnDisponibleChanged(val disponible: Boolean) : AdminPlatoEntryUiEvent
    data class OnImagenSelected(val uriString: String) : AdminPlatoEntryUiEvent
    data object OnSave : AdminPlatoEntryUiEvent
    data object ResetSuccess : AdminPlatoEntryUiEvent
}
