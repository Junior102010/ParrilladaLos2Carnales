package com.edu.ucne.parrilladalos2carnales.presentacion.menu.list

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

data class MenuUiState(
    val isLoading: Boolean = false,
    val platos: List<Plato> = emptyList(),
    val searchQuery: String = "",
    val rolUsuario: Rol = Rol.CLIENTE,
    val errorMessage: String? = null
)
