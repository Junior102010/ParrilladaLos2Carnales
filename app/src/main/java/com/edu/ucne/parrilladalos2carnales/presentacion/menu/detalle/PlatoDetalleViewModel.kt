package com.edu.ucne.parrilladalos2carnales.presentacion.menu.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.GetComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.GetGuarnicionUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatoUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta.GetOfertasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatoDetalleViewModel @Inject constructor(
    private val getPlatoUseCase: GetPlatoUseCase,
    private val getGuarnicionUseCase: GetGuarnicionUseCase,
    private val getComponenteUseCase: GetComponenteUseCase,
    private val getOfertasUseCase: GetOfertasUseCase,
    private val carritoRepository: CarritoRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlatoDetalleUiState())
    val uiState: StateFlow<PlatoDetalleUiState> = _uiState.asStateFlow()

    private var idPlatoActual: Int = 0
    private var cargarDatosJob: Job? = null

    init {
        val idPlato = savedStateHandle.get<Int>("idPlato")
            ?: savedStateHandle.get<String>("idPlato")?.toIntOrNull()
            ?: savedStateHandle.get<Int>("platoId") ?: 0
        if (idPlato != 0) setId(idPlato)
    }

    fun setId(idPlato: Int) {
        if (idPlato <= 0 || idPlatoActual == idPlato) return
        idPlatoActual = idPlato
        cargarDatos(idPlato)
    }

    fun onEvent(event: PlatoDetalleUiEvent) {
        when (event) {
            is PlatoDetalleUiEvent.OnGuarnicionSelect -> {
                _uiState.update {
                    it.copy(
                        guarnicionSeleccionada = event.guarnicion
                    )
                }
                calcularTotal()
            }

            is PlatoDetalleUiEvent.OnSalsaSelect -> {
                _uiState.update {
                    it.copy(
                        salsaSeleccionada = event.salsa
                    )
                }
                calcularTotal()
            }

            is PlatoDetalleUiEvent.OnCoccionSelect -> {
                _uiState.update {
                    it.copy(
                        terminoSeleccionado = event.termino
                    )
                }
            }

            is PlatoDetalleUiEvent.OnGuarnicionExtraToggle -> {
                toggleGuarnicionExtra(
                    event.guarnicion
                )
            }

            is PlatoDetalleUiEvent.OnSalsaExtraToggle -> {
                toggleSalsaExtra(
                    event.salsa
                )
            }

            PlatoDetalleUiEvent.OnIncrementarCantidad -> {
                _uiState.update {
                    it.copy(
                        cantidad = it.cantidad + 1
                    )
                }
                calcularTotal()
            }

            PlatoDetalleUiEvent.OnDecrementarCantidad -> {
                if (_uiState.value.cantidad > 1) {
                    _uiState.update {
                        it.copy(
                            cantidad = it.cantidad - 1
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
        cargarDatosJob = viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, agregadoExitosamente = false) }
            launch {
                getPlatoUseCase(idPlato).catch { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.localizedMessage ?: "Error al cargar el plato") }
                }.collect { plato ->
                    _uiState.update { it.copy(plato = plato, isLoading = false) }
                    calcularTotal()
                }
            }
            launch {
                getGuarnicionUseCase().catch { error ->
                    _uiState.update { it.copy(error = error.localizedMessage ?: "Error al cargar las guarniciones") }
                }.collect { guarniciones ->
                    val disponibles = guarniciones.filter { it.disponible }
                    _uiState.update { state ->
                        val seleccionada = state.guarnicionSeleccionada?.takeIf { sel -> disponibles.any { it.idGuarnicion == sel.idGuarnicion } } ?: disponibles.firstOrNull()
                        state.copy(guarnicionesDisponibles = disponibles, guarnicionSeleccionada = seleccionada)
                    }
                    calcularTotal()
                }
            }
            launch {
                getComponenteUseCase().catch { error ->
                    _uiState.update { it.copy(error = error.localizedMessage ?: "Error al cargar los componentes") }
                }.collect { componentes ->
                    val disponibles = componentes.filter { it.disponible }
                    val salsas = disponibles.filter { it.categoriaComponente.equals("Salsa", true) }
                    val terminos = disponibles.filter { it.categoriaComponente.equals("Coccion", true) }
                    _uiState.update { state ->
                        val salsaSel = state.salsaSeleccionada?.takeIf { sel -> salsas.any { it.idComponente == sel.idComponente } } ?: salsas.firstOrNull()
                        val terminoSel = state.terminoSeleccionado?.takeIf { sel -> terminos.any { it.idComponente == sel.idComponente } } ?: terminos.firstOrNull()
                        state.copy(salsasDisponibles = salsas, terminosCoccionDisponibles = terminos, salsaSeleccionada = salsaSel, terminoSeleccionado = terminoSel)
                    }
                    calcularTotal()
                }
            }
            launch {
                getOfertasUseCase().catch { error ->
                    _uiState.update { it.copy(error = error.localizedMessage ?: "Error al cargar la oferta") }
                }.collect { ofertas ->
                    val oferta = ofertas.filter { it.activa && it.idPlato == idPlato }.maxByOrNull { it.descuento }
                    _uiState.update { it.copy(ofertaActiva = oferta) }
                    calcularTotal()
                }
            }
        }
    }

    private fun toggleGuarnicionExtra(
        guarnicion: Guarnicion
    ) {
        val state = _uiState.value

        // La incluida no puede agregarse otra vez como extra
        if (
            state.guarnicionSeleccionada?.idGuarnicion ==
            guarnicion.idGuarnicion
        ) {
            return
        }

        val existe =
            state.guarnicionesExtraSeleccionadas.any {
                it.idGuarnicion == guarnicion.idGuarnicion
            }

        val nuevaLista =
            if (existe) {
                state.guarnicionesExtraSeleccionadas.filterNot {
                    it.idGuarnicion == guarnicion.idGuarnicion
                }
            } else {
                state.guarnicionesExtraSeleccionadas +
                        guarnicion
            }

        _uiState.update {
            it.copy(
                guarnicionesExtraSeleccionadas =
                    nuevaLista
            )
        }

        calcularTotal()
    }

    private fun toggleSalsaExtra(
        salsa: Componente
    ) {
        val state = _uiState.value

        if (
            state.salsaSeleccionada?.idComponente ==
            salsa.idComponente
        ) {
            return
        }

        val existe =
            state.salsasExtraSeleccionadas.any {
                it.idComponente == salsa.idComponente
            }

        val nuevaLista =
            if (existe) {
                state.salsasExtraSeleccionadas.filterNot {
                    it.idComponente == salsa.idComponente
                }
            } else {
                state.salsasExtraSeleccionadas +
                        salsa
            }

        _uiState.update {
            it.copy(
                salsasExtraSeleccionadas =
                    nuevaLista
            )
        }

        calcularTotal()
    }

    private fun precioBaseConOferta(state: PlatoDetalleUiState): Double {
        val precioBase = state.plato?.precio ?: 0.0
        val descuento = state.ofertaActiva?.descuento?.coerceIn(0.0, 100.0) ?: 0.0
        return precioBase * (1.0 - descuento / 100.0)
    }

    private fun calcularTotal() {
        val state = _uiState.value

        val precioExtras =
            state.guarnicionesExtraSeleccionadas
                .sumOf {
                    it.precioGuarnicion
                } +
            state.salsasExtraSeleccionadas
                .sumOf {
                    it.precioComponente
                }

        // IMPORTANTE:
        // la primera guarnición y primera salsa NO se suman.
        val precioUnitario =
            precioBaseConOferta(state) +
            precioExtras

        _uiState.update {
            it.copy(
                precioExtrasUnitario = precioExtras,
                precioTotal =
                    precioUnitario *
                    it.cantidad
            )
        }
    }

    private fun agregarAlCarrito() {
        val state = _uiState.value
        val plato =
            state.plato
                ?: return

        viewModelScope.launch {
            val extrasGuarnicion =
                state.guarnicionesExtraSeleccionadas
                    .sumOf {
                        it.precioGuarnicion
                    }

            val extrasSalsa =
                state.salsasExtraSeleccionadas
                    .sumOf {
                        it.precioComponente
                    }

            val precioUnitario =
                precioBaseConOferta(state) +
                extrasGuarnicion +
                extrasSalsa

            val item =
                CarritoItem(
                    idCarritoItem =
                        System.nanoTime(),
                    plato = plato,
                    termino =
                        state.terminoSeleccionado,
                    // Incluidas
                    guarnicion =
                        state.guarnicionSeleccionada,
                    salsa =
                        state.salsaSeleccionada,
                    // Extras cobrados
                    guarnicionesExtra =
                        state.guarnicionesExtraSeleccionadas,
                    salsasExtra =
                        state.salsasExtraSeleccionadas,
                    cantidad =
                        state.cantidad,
                    precioUnitario =
                        precioUnitario
                )

            carritoRepository.agregar(
                item
            )

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
