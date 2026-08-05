package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.UpsertGuarnicionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminGuarnicionViewModel @Inject constructor(
    private val upsertGuarnicionUseCase: UpsertGuarnicionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminGuarnicionUiState())
    val uiState: StateFlow<AdminGuarnicionUiState> = _uiState.asStateFlow()

    fun onEvent(event: AdminGuarnicionUiEvent) {
        when (event) {
            is AdminGuarnicionUiEvent.OnNombreChange -> {
                _uiState.update { it.copy(nombreGuarnicion = event.nombre) }
            }
            is AdminGuarnicionUiEvent.OnDescripcionChange -> {
                _uiState.update { it.copy(descripcionGuarnicion = event.descripcion) }
            }
            is AdminGuarnicionUiEvent.OnPrecioChange -> {
                _uiState.update { it.copy(precioGuarnicion = event.precio) }
            }
            is AdminGuarnicionUiEvent.OnCantidadChange -> {
                _uiState.update { it.copy(cantidadGuarnicion = event.cantidad) }
            }
            is AdminGuarnicionUiEvent.OnCategoriaChange -> {
                _uiState.update { it.copy(categoria = event.categoria) }
            }
            is AdminGuarnicionUiEvent.OnDisponibleChange -> {
                _uiState.update { it.copy(disponible = event.disponible) }
            }
            AdminGuarnicionUiEvent.OnGuardarClick -> guardarGuarnicion()
            AdminGuarnicionUiEvent.OnBackClick -> { /* Manejado en la UI */ }
        }
    }

    private fun guardarGuarnicion() {
        val state = _uiState.value
        val precio = state.precioGuarnicion.toDoubleOrNull() ?: 0.0

        if (state.nombreGuarnicion.isBlank()) {
            _uiState.update { it.copy(error = "El nombre no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val nuevaGuarnicion = Guarnicion(
                    idGuarnicion = state.idGuarnicion,
                    nombreGuarnicion = state.nombreGuarnicion,
                    descripcionGuarnicion = state.descripcionGuarnicion,
                    cantidadGuarnicion = state.cantidadGuarnicion,
                    precioGuarnicion = precio,
                    disponible = state.disponible,
                    categoria = state.categoria
                )
                upsertGuarnicionUseCase(nuevaGuarnicion)
                _uiState.update { it.copy(isLoading = false, guardadoExitoso = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
            }
        }
    }
}