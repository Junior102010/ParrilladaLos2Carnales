package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.GetComponenteUseCase
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.DeleteComponenteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminComponenteListViewModel @Inject constructor(
    private val getComponenteUseCase: GetComponenteUseCase,
    private val deleteComponenteUseCase: DeleteComponenteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminComponenteListUiState())
    val uiState: StateFlow<AdminComponenteListUiState> = _uiState.asStateFlow()

    init {
        cargarComponentes()
    }

    fun onEvent(event: AdminComponenteListUiEvent) {
        when (event) {
            is AdminComponenteListUiEvent.OnDeleteComponenteClick -> borrarComponente(event.componente)
            AdminComponenteListUiEvent.OnAddComponenteClick -> {  }
            is AdminComponenteListUiEvent.OnEditComponenteClick -> {  }
            AdminComponenteListUiEvent.OnBackClick -> {  }
        }
    }

    private fun cargarComponentes() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            getComponenteUseCase()
                .catch { e ->
                    _uiState.update { it.copy(error = e.localizedMessage, isLoading = false) }
                }
                .collect { lista ->
                    _uiState.update { it.copy(componentes = lista, isLoading = false) }
                }
        }
    }

    private fun borrarComponente(componente: Componente) {
        viewModelScope.launch {
            deleteComponenteUseCase(componente.idComponente)
        }
    }
}
