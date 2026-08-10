package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente

data class AdminComponenteListUiState(
    val isLoading: Boolean = false,
    val componentes: List<Componente> = emptyList(),
    val error: String? = null
)
