package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion.list

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion

data class AdminGuarnicionListUiState(
    val isLoading: Boolean = false,
    val guarniciones: List<Guarnicion> = emptyList(),
    val guarnicionesFiltradas: List<Guarnicion> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)
