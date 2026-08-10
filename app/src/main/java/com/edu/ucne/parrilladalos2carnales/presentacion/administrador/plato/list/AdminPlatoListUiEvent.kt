package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

sealed interface AdminPlatoListUiEvent {
    data class OnSearchQueryChanged(val query: String) : AdminPlatoListUiEvent
    data class OnToggleDisponible(val plato: Plato, val disponible: Boolean) : AdminPlatoListUiEvent
    data class OnDeletePlato(val plato: Plato) : AdminPlatoListUiEvent
}
