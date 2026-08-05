package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.GetComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.GetGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatoDetalleViewModel @Inject constructor(
    private val getPlatoUseCase: GetPlatoUseCase,
    private val getGuarnicionUseCase: GetGuarnicionUseCase,
    private val getComponenteUseCase: GetComponenteUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlatoDetalleUiState())
    val uiState: StateFlow<PlatoDetalleUiState> = _uiState.asStateFlow()

    init {
        // Buscamos el ID en el SavedStateHandle (soporta varios nombres de llave)
        val idPlato = savedStateHandle.get<Int>("idPlato") 
            ?: savedStateHandle.get<String>("idPlato")?.toIntOrNull()
            ?: savedStateHandle.get<Int>("platoId") 
            ?: 0
        
        if (idPlato != 0) {
            cargarDatos(idPlato)
        }
    }

    fun setId(idPlato: Int) {
        if (_uiState.value.plato == null || _uiState.value.plato?.idPlato != idPlato) {
            cargarDatos(idPlato)
        }
    }

    fun onEvent(event: PlatoDetalleUiEvent) {
        when (event) {
            is PlatoDetalleUiEvent.OnGuarnicionSelect -> {
                _uiState.update { it.copy(guarnicionSeleccionada = event.guarnicion) }
                calcularTotal()
            }
            is PlatoDetalleUiEvent.OnSalsaSelect -> {
                _uiState.update { it.copy(salsaSeleccionada = event.salsa) }
                calcularTotal()
            }
            is PlatoDetalleUiEvent.OnCoccionSelect -> {
                _uiState.update { it.copy(terminoSeleccionado = event.termino) }
            }
            PlatoDetalleUiEvent.OnIncrementarCantidad -> {
                _uiState.update { it.copy(cantidad = it.cantidad + 1) }
                calcularTotal()
            }
            PlatoDetalleUiEvent.OnDecrementarCantidad -> {
                if (_uiState.value.cantidad > 1) {
                    _uiState.update { it.copy(cantidad = it.cantidad - 1) }
                    calcularTotal()
                }
            }
            PlatoDetalleUiEvent.OnAgregarAlCarrito -> {
                _uiState.update { it.copy(agregadoExitosamente = true) }
            }
        }
    }

    private fun cargarDatos(idPlato: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            launch {
                getPlatoUseCase(idPlato)
                    .catch { e -> _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) } }
                    .collect { plato ->
                        _uiState.update { it.copy(
                            plato = plato, 
                            precioTotal = plato?.precio ?: 0.0,
                            isLoading = false // Quitamos el loading al recibir el plato
                        ) }
                        calcularTotal()
                    }
            }

            launch {
                getGuarnicionUseCase()
                    .catch { e -> _uiState.update { it.copy(error = e.localizedMessage) } }
                    .collect { guarniciones ->
                        val disponibles = guarniciones.filter { it.disponible }
                        _uiState.update { state ->
                            state.copy(
                                guarnicionesDisponibles = disponibles,
                                guarnicionSeleccionada = state.guarnicionSeleccionada ?: disponibles.firstOrNull()
                            )
                        }
                        calcularTotal()
                    }
            }

            launch {
                getComponenteUseCase()
                    .catch { e -> _uiState.update { it.copy(error = e.localizedMessage) } }
                    .collect { componentes ->
                        val disponibles = componentes.filter { it.disponible }
                        val salsas = disponibles.filter { it.categoriaComponente.equals("Salsa", ignoreCase = true) }
                        val terminos = disponibles.filter { it.categoriaComponente.equals("Coccion", ignoreCase = true) }

                        _uiState.update { state ->
                            state.copy(
                                salsasDisponibles = salsas,
                                terminosCoccionDisponibles = terminos,
                                salsaSeleccionada = state.salsaSeleccionada ?: salsas.firstOrNull(),
                                terminoSeleccionado = state.terminoSeleccionado ?: terminos.firstOrNull()
                            )
                        }
                        // Solo quitamos loading si el plato ya llegó o si falló
                        if (_uiState.value.plato != null || _uiState.value.error != null) {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                        calcularTotal()
                    }
            }
        }
    }

    private fun calcularTotal() {
        val state = _uiState.value
        val precioBase = state.plato?.precio ?: 0.0
        val precioGuarnicion = state.guarnicionSeleccionada?.precioGuarnicion ?: 0.0
        val precioSalsa = state.salsaSeleccionada?.precioComponente ?: 0.0

        val total = (precioBase + precioGuarnicion + precioSalsa) * state.cantidad
        _uiState.update { it.copy(precioTotal = total) }
    }
}