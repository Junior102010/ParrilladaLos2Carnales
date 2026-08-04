package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.plato.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.DeletePlatoUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.plato.GetPlatosUseCase
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
    private val deletePlatoUseCase: DeletePlatoUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminPlatoListUiState())
    val uiState: StateFlow<AdminPlatoListUiState> = _uiState.asStateFlow()

    init {
        loadPlatos()
    }

    fun onEvent(event: AdminPlatoListUiEvent) {
        when (event) {
            is AdminPlatoListUiEvent.OnDeletePlato -> {
                viewModelScope.launch {
                    deletePlatoUseCase(event.plato)
                }
            }
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
                    _uiState.update { it.copy(isLoading = false, platos = lista) }
                }
        }
    }
}