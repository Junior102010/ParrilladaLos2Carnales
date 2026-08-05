package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion

sealed interface AdminGuarnicionUiEvent {
    data class OnNombreChange(val nombre: String) : AdminGuarnicionUiEvent
    data class OnDescripcionChange(val descripcion: String) : AdminGuarnicionUiEvent
    data class OnPrecioChange(val precio: String) : AdminGuarnicionUiEvent
    data class OnCantidadChange(val cantidad: Double) : AdminGuarnicionUiEvent
    data class OnCategoriaChange(val categoria: String) : AdminGuarnicionUiEvent
    data class OnDisponibleChange(val disponible: Boolean) : AdminGuarnicionUiEvent
    data object OnGuardarClick : AdminGuarnicionUiEvent
    data object OnBackClick : AdminGuarnicionUiEvent
}