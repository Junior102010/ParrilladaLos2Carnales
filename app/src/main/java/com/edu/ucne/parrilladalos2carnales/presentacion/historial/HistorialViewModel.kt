package com.edu.ucne.parrilladalos2carnales.presentacion.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.login.AuthRepository
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.GetComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.GetGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistorialViewModel @Inject constructor(
    private val pedidoRepository: PedidoRepository,
    private val authRepository: AuthRepository,
    private val carritoRepository: CarritoRepository,
    private val getPlatoUseCase: GetPlatoUseCase,
    private val getGuarnicionUseCase: GetGuarnicionUseCase,
    private val getComponenteUseCase: GetComponenteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistorialUiState())
    val uiState = _uiState.asStateFlow()

    private var historialJob: kotlinx.coroutines.Job? = null

    init {
        refrescarHistorial()
    }

    fun refrescarHistorial() {
        historialJob?.cancel()
        val uid = authRepository.getUsuarioUid()
        if (uid.isNullOrBlank()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "No hay una sesión iniciada") }
            return
        }
        historialJob = viewModelScope.launch {
            pedidoRepository.getPedidosPorUsuario(uid)
                .catch { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = error.localizedMessage ?: "Error al cargar historial") }
                }
                .collect { pedidos ->
                    _uiState.update { it.copy(isLoading = false, pedidos = pedidos, errorMessage = null) }
                }
        }
    }

    fun repetirPedido(pedido: Pedido) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val guarniciones = getGuarnicionUseCase().first()
                val componentes = getComponenteUseCase().first()

                pedido.detalles.forEach { detalle ->
                    val plato = getPlatoUseCase(detalle.idPlato).first() ?: return@forEach
                    
                    val guarnicion = detalle.idGuarnicion?.let { id -> guarniciones.find { it.idGuarnicion == id } }
                    val termino = detalle.idTermino?.let { id -> componentes.find { it.idComponente == id } }
                    val salsa = detalle.idSalsa?.let { id -> componentes.find { it.idComponente == id } }

                    val item = CarritoItem(
                        idCarritoItem = System.nanoTime(),
                        plato = plato,
                        termino = termino,
                        guarnicion = guarnicion,
                        salsa = salsa,
                        cantidad = detalle.cantidad,
                        precioUnitario = detalle.precioUnitario
                    )
                    carritoRepository.agregar(item)
                }
                _uiState.update { it.copy(isLoading = false, pedidoRepetidoExitosamente = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "No se pudo repetir el pedido") }
            }
        }
    }

    fun onMensajeConsumido() {
        _uiState.update { it.copy(pedidoRepetidoExitosamente = false) }
    }
}
