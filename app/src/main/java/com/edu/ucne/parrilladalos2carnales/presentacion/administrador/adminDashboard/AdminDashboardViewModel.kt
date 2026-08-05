package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.obtenerFechaHoy
import com.edu.ucne.parrilladalos2carnales.domain.useCase.categoria.GetCategoriasUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminDashboardViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    // Calcula el total de ventas del día y los pedidos activos
    fun calcularVentasYPedidos(pedidos: List<Pedido>) {
        val hoy = obtenerFechaHoy()

        // Ventas de Hoy: Suma del costo total de pedidos realizados hoy (excluyendo cancelados)
        val totalVentasHoy = pedidos
            .filter { it.fecha == hoy && it.estado != EstadoPedido.CANCELADO }
            .sumOf { it.total }

        // Pedidos Activos: Cantidad de pedidos pendientes o en proceso
        val activosCount = pedidos.count { it.estado.esActivo }

        val pedidosTotalHoy = pedidos.count { it.fecha == hoy }

        _uiState.update { state ->
            state.copy(
                ventasHoy = totalVentasHoy,
                pedidosActivosCount = activosCount,
                pedidosTotalHoy = pedidosTotalHoy,
                pedidos = pedidos
            )
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            combine(
                getPlatosUseCase(),
                getCategoriasUseCase()
            ) { platosList, categoriasList ->
                val catMap = categoriasList.associate { it.idCategoria to it.nombreCategoria }
                _uiState.update { state ->
                    state.copy(
                        isLoading = false,
                        platos = platosList,
                        categoriasMap = catMap
                    )
                }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
            }.collect {}
        }
    }
}