package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.GetComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.GetGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
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
    private val carritoRepository: CarritoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PlatoDetalleUiState())

    val uiState: StateFlow<PlatoDetalleUiState> =
        _uiState.asStateFlow()


    private var idPlatoActual: Int = 0

    private var cargarDatosJob: Job? = null

    init {

        val idPlato =
            savedStateHandle.get<Int>("idPlato")
                ?: savedStateHandle
                    .get<String>("idPlato")
                    ?.toIntOrNull()
                ?: savedStateHandle.get<Int>("platoId")
                ?: 0

        if (idPlato != 0) {
            setId(idPlato)
        }
    }

    fun setId(idPlato: Int) {

        if (idPlato <= 0) {
            return
        }

        if (idPlatoActual == idPlato) {
            return
        }

        idPlatoActual = idPlato

        cargarDatos(idPlato)
    }

    fun onEvent(event: PlatoDetalleUiEvent) {

        when (event) {

            is PlatoDetalleUiEvent.OnGuarnicionSelect -> {

                _uiState.update {
                    it.copy(
                        guarnicionSeleccionada =
                            event.guarnicion
                    )
                }

                calcularTotal()
            }

            is PlatoDetalleUiEvent.OnSalsaSelect -> {

                _uiState.update {
                    it.copy(
                        salsaSeleccionada =
                            event.salsa
                    )
                }

                calcularTotal()
            }

            is PlatoDetalleUiEvent.OnCoccionSelect -> {

                _uiState.update {
                    it.copy(
                        terminoSeleccionado =
                            event.termino
                    )
                }

            }

            PlatoDetalleUiEvent.OnIncrementarCantidad -> {

                _uiState.update {
                    it.copy(
                        cantidad =
                            it.cantidad + 1
                    )
                }

                calcularTotal()
            }

            PlatoDetalleUiEvent.OnDecrementarCantidad -> {

                if (_uiState.value.cantidad > 1) {

                    _uiState.update {
                        it.copy(
                            cantidad =
                                it.cantidad - 1
                        )
                    }

                    calcularTotal()
                }
            }

            PlatoDetalleUiEvent.OnAgregarAlCarrito -> {

                agregarAlCarrito()
            }

            PlatoDetalleUiEvent.OnAgregarConsumido -> {

                _uiState.update {
                    it.copy(
                        agregadoExitosamente = false
                    )
                }
            }
        }
    }


    private fun cargarDatos(idPlato: Int) {


        cargarDatosJob?.cancel()

        cargarDatosJob =
            viewModelScope.launch {

                _uiState.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                        agregadoExitosamente = false
                    )
                }


                launch {

                    getPlatoUseCase(idPlato)
                        .catch { error ->

                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error =
                                        error.localizedMessage
                                            ?: "Error al cargar el plato"
                                )
                            }
                        }
                        .collect { plato ->

                            _uiState.update {
                                it.copy(
                                    plato = plato,
                                    isLoading = false
                                )
                            }

                            calcularTotal()
                        }
                }


                launch {

                    getGuarnicionUseCase()
                        .catch { error ->

                            _uiState.update {
                                it.copy(
                                    error =
                                        error.localizedMessage
                                            ?: "Error al cargar las guarniciones"
                                )
                            }
                        }
                        .collect { guarniciones ->

                            val disponibles =
                                guarniciones.filter {
                                    it.disponible
                                }

                            _uiState.update { state ->

                                val seleccionada =
                                    state.guarnicionSeleccionada
                                        ?.takeIf { seleccionActual ->

                                            disponibles.any {
                                                it.idGuarnicion ==
                                                        seleccionActual.idGuarnicion
                                            }
                                        }
                                        ?: disponibles.firstOrNull()

                                state.copy(
                                    guarnicionesDisponibles =
                                        disponibles,

                                    guarnicionSeleccionada =
                                        seleccionada
                                )
                            }

                            calcularTotal()
                        }
                }

                launch {

                    getComponenteUseCase()
                        .catch { error ->

                            _uiState.update {
                                it.copy(
                                    error =
                                        error.localizedMessage
                                            ?: "Error al cargar los componentes"
                                )
                            }
                        }
                        .collect { componentes ->

                            val disponibles =
                                componentes.filter {
                                    it.disponible
                                }

                            val salsas =
                                disponibles.filter {

                                    it.categoriaComponente
                                        .equals(
                                            "Salsa",
                                            ignoreCase = true
                                        )
                                }

                            val terminos =
                                disponibles.filter {

                                    it.categoriaComponente
                                        .equals(
                                            "Coccion",
                                            ignoreCase = true
                                        )
                                }

                            _uiState.update { state ->

                                val salsaSeleccionada =
                                    state.salsaSeleccionada
                                        ?.takeIf { seleccionActual ->

                                            salsas.any {
                                                it.idComponente ==
                                                        seleccionActual.idComponente
                                            }
                                        }
                                        ?: salsas.firstOrNull()

                                val terminoSeleccionado =
                                    state.terminoSeleccionado
                                        ?.takeIf { seleccionActual ->

                                            terminos.any {
                                                it.idComponente ==
                                                        seleccionActual.idComponente
                                            }
                                        }
                                        ?: terminos.firstOrNull()

                                state.copy(
                                    salsasDisponibles =
                                        salsas,

                                    terminosCoccionDisponibles =
                                        terminos,

                                    salsaSeleccionada =
                                        salsaSeleccionada,

                                    terminoSeleccionado =
                                        terminoSeleccionado
                                )
                            }

                            calcularTotal()
                        }
                }
            }
    }


    private fun calcularTotal() {

        val state =
            _uiState.value

        val precioBase =
            state.plato?.precio
                ?: 0.0

        val precioGuarnicion =
            state.guarnicionSeleccionada
                ?.precioGuarnicion
                ?: 0.0

        val precioSalsa =
            state.salsaSeleccionada
                ?.precioComponente
                ?: 0.0

        val precioUnitario =
            precioBase +
                    precioGuarnicion +
                    precioSalsa

        val total =
            precioUnitario *
                    state.cantidad

        _uiState.update {
            it.copy(
                precioTotal = total
            )
        }
    }

    private fun agregarAlCarrito() {

        val state = _uiState.value

        val plato = state.plato ?: return

        viewModelScope.launch {

            val precioUnitario =
                plato.precio +
                        (state.guarnicionSeleccionada
                            ?.precioGuarnicion ?: 0.0) +
                        (state.salsaSeleccionada
                            ?.precioComponente ?: 0.0)

            val item = CarritoItem(
                idCarritoItem =
                System.nanoTime(),

                plato = plato,

                termino =
                state.terminoSeleccionado,

                guarnicion =
                state.guarnicionSeleccionada,

                salsa =
                state.salsaSeleccionada,

                cantidad =
                state.cantidad,

                precioUnitario =
                precioUnitario
            )

            carritoRepository.agregar(item)

            _uiState.update {
                it.copy(
                    agregadoExitosamente = true
                )
            }
        }
    }

    override fun onCleared() {

        cargarDatosJob?.cancel()

        super.onCleared()
    }
}