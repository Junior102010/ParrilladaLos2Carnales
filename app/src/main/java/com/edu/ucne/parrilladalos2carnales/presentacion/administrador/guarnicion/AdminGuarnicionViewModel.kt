package com.edu.ucne.parrilladalos2carnales.presentacion.administrador.guarnicion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
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
    private val upsertGuarnicionUseCase: UpsertGuarnicionUseCase,
    private val guarnicionRepository: GuarnicionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdminGuarnicionUiState())
    val uiState: StateFlow<AdminGuarnicionUiState> = _uiState.asStateFlow()

    fun prepararEntrada(idGuarnicion: Int) {
        if (idGuarnicion == 0) {
            _uiState.value = AdminGuarnicionUiState()
        } else {
            _uiState.value = AdminGuarnicionUiState(isLoading = true)
            cargarGuarnicion(idGuarnicion)
        }
    }

    fun consumirGuardadoExitoso() {
        _uiState.update {
            it.copy(
                guardadoExitoso = false
            )
        }
    }

    private fun cargarGuarnicion(id: Int) {
        viewModelScope.launch {
            val guarnicion = guarnicionRepository.getGuarnicion(id)
            if (guarnicion != null) {
                _uiState.update {
                    it.copy(
                        idGuarnicion = guarnicion.idGuarnicion,
                        nombreGuarnicion = guarnicion.nombreGuarnicion,
                        descripcionGuarnicion = guarnicion.descripcionGuarnicion,
                        precioGuarnicion = guarnicion.precioGuarnicion.toString(),
                        cantidadGuarnicion = guarnicion.cantidadGuarnicion,
                        categoria = guarnicion.categoria,
                        disponible = guarnicion.disponible,
                        isLoading = false,
                        guardadoExitoso = false,
                        error = null
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

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
            AdminGuarnicionUiEvent.OnBackClick -> {  }
            AdminGuarnicionUiEvent.ResetSuccess -> consumirGuardadoExitoso()
        }
    }

    private fun guardarGuarnicion() {
        val state = _uiState.value
        val precio = state.precioGuarnicion.toDoubleOrNull()

        if (state.nombreGuarnicion.isBlank()) {
            _uiState.update { it.copy(error = "El nombre no puede estar vacío") }
            return
        }

        if (precio == null) {
            _uiState.update { it.copy(error = "Introduce un precio válido") }
            return
        }

        if (precio < 0) {
            _uiState.update { it.copy(error = "El precio adicional no puede ser negativo") }
            return
        }

        if (state.cantidadGuarnicion < 0) {
            _uiState.update { it.copy(error = "La cantidad no puede ser negativa") }
            return
        }

        if (state.categoria.isBlank()) {
            _uiState.update { it.copy(error = "Selecciona una categoría") }
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
                val result = upsertGuarnicionUseCase(nuevaGuarnicion)
                
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
