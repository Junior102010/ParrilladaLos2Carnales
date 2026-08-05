package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion

sealed interface PlatoDetalleUiEvent {
    data class OnGuarnicionSelect(val guarnicion: Guarnicion) : PlatoDetalleUiEvent
    data class OnSalsaSelect(val salsa: Componente) : PlatoDetalleUiEvent
    data class OnCoccionSelect(val termino: Componente) : PlatoDetalleUiEvent
    data object OnIncrementarCantidad : PlatoDetalleUiEvent
    data object OnDecrementarCantidad : PlatoDetalleUiEvent
    data object OnAgregarAlCarrito : PlatoDetalleUiEvent
}