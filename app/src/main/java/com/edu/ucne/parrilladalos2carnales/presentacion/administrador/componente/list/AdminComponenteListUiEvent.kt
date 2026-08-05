package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente

sealed interface AdminComponenteListUiEvent {
    data object OnAddComponenteClick : AdminComponenteListUiEvent
    data class OnEditComponenteClick(val idComponente: Int) : AdminComponenteListUiEvent
    data class OnDeleteComponenteClick(val componente: Componente) : AdminComponenteListUiEvent
    data object OnBackClick : AdminComponenteListUiEvent
}