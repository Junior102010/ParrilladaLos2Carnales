package com.edu.ucne.parrilladalos2carnales.presentacion.plato.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import javax.inject.Inject

@HiltViewModel
class PlatoListViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(PlatoListUiState())

    val uiState: StateFlow<PlatoListUiState> =
        _uiState.asStateFlow()

    init {
        cargarPlatos()
    }

    fun onEvent(event: PlatoListUiEvent) {

        when (event) {

            is PlatoListUiEvent.OnSearchChange -> {
                buscarPlatos(event.query)
            }

            is PlatoListUiEvent.OnPlatoClicked -> {
                // La navegación se maneja desde MenuScreen.
            }

            is PlatoListUiEvent.OnAddCarritoClicked -> {
                // Luego conectaremos esto con el carrito.
            }

            PlatoListUiEvent.OnRefresh -> {
                cargarPlatos()
            }
        }
    }

    private fun cargarPlatos() {

        viewModelScope.launch {

            _uiState.update {
                it.copy(isLoading = true)
            }

            getPlatosUseCase()
                .catch { error ->

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage =
                                error.localizedMessage
                        )
                    }
                }
                .collect { lista ->

                    // El cliente solo ve platos disponibles.
                    val disponibles =
                        lista.filter { plato ->
                            plato.disponible
                        }

                    val query =
                        _uiState.value.searchQuery

                    val filtrados =
                        filtrar(
                            platos = disponibles,
                            query = query
                        )

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            platos = disponibles,
                            platosFiltrados = filtrados
                        )
                    }
                }
        }
    }

    private fun buscarPlatos(query: String) {

        val filtrados =
            filtrar(
                platos = _uiState.value.platos,
                query = query
            )

        _uiState.update {
            it.copy(
                searchQuery = query,
                platosFiltrados = filtrados
            )
        }
    }

    private fun filtrar(
        platos: List<Plato>,
        query: String
    ): List<Plato> {

        if (query.isBlank()) {
            return platos
        }

        return platos.filter { plato ->

            plato.nombre.contains(
                query,
                ignoreCase = true
            ) ||
                    plato.descripcion.contains(
                        query,
                        ignoreCase = true
                    )
        }
    }
}