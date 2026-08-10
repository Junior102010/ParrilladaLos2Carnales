package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.DeletePlatoUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.UpsertPlatoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminPlatoListViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase,
    private val upsertPlatoUseCase: UpsertPlatoUseCase,
    private val deletePlatoUseCase: DeletePlatoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPlatoListUiState())
    val uiState: StateFlow<AdminPlatoListUiState> = _uiState.asStateFlow()

    init {
        loadPlatos()
    }

    fun onEvent(event: AdminPlatoListUiEvent) {
        when (event) {
            is AdminPlatoListUiEvent.OnSearchQueryChanged -> {
                _uiState.update { state ->
                    val query = event.query
                    val filtrados = state.platos.filter {
                        it.nombre.contains(query, ignoreCase = true) ||
                                it.descripcion.contains(query, ignoreCase = true)
                    }
                    state.copy(searchQuery = query, platosFiltrados = filtrados)
                }
            }
            is AdminPlatoListUiEvent.OnToggleDisponible -> {
                viewModelScope.launch {
                    val platoActualizado = event.plato.copy(disponible = event.disponible)
                    upsertPlatoUseCase(platoActualizado)
                }
            }
            is AdminPlatoListUiEvent.OnDeletePlato -> {
                viewModelScope.launch {
                    deletePlatoUseCase(event.plato)
                }
            }
            else -> {}
        }
    }

    private fun loadPlatos() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getPlatosUseCase()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage) }
                }
                .collect { lista ->
                    _uiState.update { state ->
                        val filtrados = if (state.searchQuery.isBlank()) lista else lista.filter {
                            it.nombre.contains(state.searchQuery, ignoreCase = true)
                        }
                        state.copy(isLoading = false, platos = lista, platosFiltrados = filtrados)
                    }
                }
        }
    }
}
