package com.edu.ucne.parrilladalos2carnales.presentacion.plato.list

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

sealed interface PlatoListUiEvent {

    data class OnPlatoClicked(
        val idPlato: Int
    ) : PlatoListUiEvent

    data class OnAddCarritoClicked(
        val plato: Plato
    ) : PlatoListUiEvent

    data class OnSearchChange(
        val query: String
    ) : PlatoListUiEvent

    data object OnRefresh : PlatoListUiEvent
}
