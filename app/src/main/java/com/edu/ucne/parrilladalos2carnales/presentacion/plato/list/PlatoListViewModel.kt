package com.edu.ucne.parrilladalos2carnales.presentacion.plato.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlatoListViewModel @Inject constructor(
    private val getPlatosUseCase: GetPlatosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlatoListUiState())
    val uiState: StateFlow<PlatoListUiState> = _uiState.asStateFlow()

    init {
        cargarPlatos()
    }

    fun onEvent(event: PlatoListUiEvent) {
        when (event) {
            is PlatoListUiEvent.OnPlatoClicked -> {
            }
            is PlatoListUiEvent.OnAddCarritoClicked -> {
            }
            PlatoListUiEvent.OnRefresh -> {
                cargarPlatos()
            }
        }
    }

    private fun cargarPlatos() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getPlatosUseCase().collect { listaPlatos ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        platos = listaPlatos
                    )
                }
            }
        }
    }
}