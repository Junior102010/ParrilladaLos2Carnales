package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.oferta

import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class AdminOfertaUiState(
    val ofertas: List<Oferta> = emptyList(),
    val ofertasFiltradas: List<Oferta> = emptyList(),
    val platos: List<Plato> = emptyList(),

    val searchQuery: String = "",

    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessage: String? = null,

    val editorVisible: Boolean = false,

    val idOfertaEditando: Int = 0,
    val tituloOferta: String = "",
    val descripcionOferta: String = "",
    val descuento: String = "",
    val idPlatoSeleccionado: Int? = null,
    val activa: Boolean = true
)