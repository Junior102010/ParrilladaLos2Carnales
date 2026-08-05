package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminDashboard

import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

data class AdminDashboardUiState(
    val ventasHoy: Double = 0.0,
    val pedidosActivosCount: Int = 0,
    val pedidosTotalHoy: Int = 0,
    val pedidosCapacidadMax: Int = 20,
    val pedidos: List<Pedido> = emptyList(),
    val platos: List<Plato> = emptyList(),
    val categoriasMap: Map<Int, String> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)