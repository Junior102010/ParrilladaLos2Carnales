package com.edu.ucne.parrilladalos2carnales.presentacion.plato.list

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class PlatoListUiState(
    val isLoading: Boolean = false,
    val platos: List<Plato> = emptyList(),
    val errorMessage: String? = null
)