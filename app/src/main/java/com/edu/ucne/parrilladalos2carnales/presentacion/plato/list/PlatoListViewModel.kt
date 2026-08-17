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

    private var categoriaSeleccionada: Int? = null

    init {
        cargarPlatos()
    }

    fun setCategoria(idCategoria: Int?) {
        categoriaSeleccionada = idCategoria
        aplicarFiltros()
    }

    fun onEvent(event: PlatoListUiEvent) {
        when (event) {
            is PlatoListUiEvent.OnSearchChange -> {
                buscarPlatos(event.query)
            }
            is PlatoListUiEvent.OnPlatoClicked -> {}
            is PlatoListUiEvent.OnAddCarritoClicked -> {}
            PlatoListUiEvent.OnRefresh -> {
                cargarPlatos()
            }
        }
    }

    private fun cargarPlatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getPlatosUseCase()
                .catch { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = error.localizedMessage
                        )
                    }
                }
                .collect { lista ->
                    val disponibles = lista.filter { it.disponible }
                    
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            platos = disponibles
                        )
                    }
                    aplicarFiltros()
                }
        }
    }

    private fun buscarPlatos(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        aplicarFiltros()
    }

    private fun aplicarFiltros() {
        val query = _uiState.value.searchQuery
        val filtrados = filtrar(
            platos = _uiState.value.platos,
            query = query
        )

        _uiState.update {
            it.copy(platosFiltrados = filtrados)
        }
    }

    private fun filtrar(
        platos: List<Plato>,
        query: String
    ): List<Plato> {
        return platos.filter { plato ->
            val coincideCategoria = categoriaSeleccionada == null || 
                                   plato.idCategoria == categoriaSeleccionada

            val coincideBusqueda = query.isBlank() ||
                    plato.nombre.contains(query, ignoreCase = true) ||
                    plato.descripcion.contains(query, ignoreCase = true)

            coincideCategoria && coincideBusqueda
        }
    }
}
