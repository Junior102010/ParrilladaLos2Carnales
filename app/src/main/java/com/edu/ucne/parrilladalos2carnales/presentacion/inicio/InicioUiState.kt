package com.edu.ucne.parrilladalos2carnales.presentacion.inicio

import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

data class InicioUiState(
    val isLoading: Boolean = false,
    val ofertas: List<Oferta> = emptyList(),
    val categorias: List<Categoria> = emptyList(),

    val platos: List<Plato> = emptyList(),

    val rolUsuario: Rol = Rol.CLIENTE,
    val errorMessage: String? = null
)