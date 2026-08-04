package com.edu.ucne.parrilladalos2carnales.presentacion.menu

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

sealed interface MenuUiEvent {
    data class OnSearchQueryChanged(val query: String) : MenuUiEvent
    data class OnAddToCart(val plato: Plato) : MenuUiEvent
}