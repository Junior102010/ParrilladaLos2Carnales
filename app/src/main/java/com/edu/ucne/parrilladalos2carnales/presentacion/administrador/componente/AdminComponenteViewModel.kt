package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.componente

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.UpsertComponenteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminComponenteViewModel @Inject constructor(
    private val upsertComponenteUseCase: UpsertComponenteUseCase,
    private val componenteRepository: ComponenteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminComponenteUiState())
    val uiState: StateFlow<AdminComponenteUiState> = _uiState.asStateFlow()

    init {
        val componenteId = savedStateHandle.get<Int>("idComponente") ?: 0
        if (componenteId > 0) {
            cargarComponente(componenteId)
        }
    }

    private fun cargarComponente(id: Int) {
        viewModelScope.launch {
            val componente = componenteRepository.getComponente(id)
            if (componente != null) {
                _uiState.update {
                    it.copy(
                        idComponente = componente.idComponente,
                        nombreComponente = componente.nombreComponente,
                        descripcionComponente = componente.descripcionComponente,
                        precioComponente = componente.precioComponente.toString(),
                        cantidadComponente = componente.cantidadComponente,
                        categoriaComponente = componente.categoriaComponente,
                        coccion = componente.coccion ?: "",
                        disponible = componente.disponible
                    )
                }
            }
        }
    }

    fun onEvent(event: AdminComponenteUiEvent) {
        when (event) {
            is AdminComponenteUiEvent.OnNombreChange -> _uiState.update { it.copy(nombreComponente = event.nombre) }
            is AdminComponenteUiEvent.OnDescripcionChange -> _uiState.update { it.copy(descripcionComponente = event.descripcion) }
            is AdminComponenteUiEvent.OnCantidadChange -> _uiState.update { it.copy(cantidadComponente = event.cantidad) }
            is AdminComponenteUiEvent.OnPrecioChange -> _uiState.update { it.copy(precioComponente = event.precio) }
            is AdminComponenteUiEvent.OnCategoriaChange -> _uiState.update {
                it.copy(
                    categoriaComponente = event.categoria,
                    // Si cambia a Cocción, el precio por defecto suele ser 0
                    precioComponente = if (event.categoria == "Coccion") "0.0" else it.precioComponente
                )
            }
            is AdminComponenteUiEvent.OnCoccionChange -> _uiState.update { it.copy(coccion = event.coccion) }
            is AdminComponenteUiEvent.OnDisponibleChange -> _uiState.update { it.copy(disponible = event.disponible) }
            AdminComponenteUiEvent.OnGuardarClick -> guardarComponente()
            AdminComponenteUiEvent.OnBackClick -> { /* Manejado por navegación */ }
        }
    }

    private fun guardarComponente() {
        val state = _uiState.value
        val precio = state.precioComponente.toDoubleOrNull() ?: 0.0

        if (state.nombreComponente.isBlank()) {
            _uiState.update { it.copy(error = "El nombre no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val nuevoComponente = Componente(
                    idComponente = state.idComponente,
                    nombreComponente = state.nombreComponente,
                    descripcionComponente = state.descripcionComponente,
                    cantidadComponente = state.cantidadComponente,
                    precioComponente = precio,
                    categoriaComponente = state.categoriaComponente,
                    coccion = state.coccion,
                    disponible = state.disponible
                )
                val result = upsertComponenteUseCase(nuevoComponente)
                
                result.onSuccess {
                    _uiState.update { it.copy(isLoading = false, guardadoExitoso = true) }
                }.onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}