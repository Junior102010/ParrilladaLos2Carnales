package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.adminDashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.obtenerFechaHoy
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
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
    private val pedidoRepository: PedidoRepository,
    private val getPlatosUseCase: GetPlatosUseCase,
    private val getCategoriasUseCase: GetCategoriasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminDashboardUiState())
    val uiState: StateFlow<AdminDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            combine(
                pedidoRepository.getPedidos(),
                getPlatosUseCase(),
                getCategoriasUseCase()
            ) { pedidos, platos, categorias ->
                Triple(
                    pedidos,
                    platos,
                    categorias
                )
            }
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage
                                ?: "No se pudo cargar el Dashboard"
                        )
                    }
                }
                .collect { (pedidos, platos, categorias) ->
                    val hoy = obtenerFechaHoy()

                    val pedidosHoy =
                        pedidos.filter {
                            it.fecha == hoy
                        }

                    val ventasHoy =
                        pedidosHoy
                            .filter {
                                it.estado != EstadoPedido.CANCELADO
                            }
                            .sumOf {
                                it.total
                            }

                    val pedidosActivos =
                        pedidos.count {
                            it.estado.esActivo
                        }

                    val categoriasMap =
                        categorias.associate {
                            it.idCategoria to
                                    it.nombreCategoria
                        }

                    _uiState.update {
                        it.copy(
                            ventasHoy = ventasHoy,
                            pedidosActivosCount = pedidosActivos,
                            pedidosTotalHoy = pedidosHoy.size,
                            pedidos = pedidos,
                            platos = platos,
                            categoriasMap = categoriasMap,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
        }
    }
}
