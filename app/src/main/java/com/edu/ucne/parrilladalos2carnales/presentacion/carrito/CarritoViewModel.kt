package com.edu.ucne.parrilladalos2carnales.presentacion.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarritoViewModel @Inject constructor(
    private val carritoRepository:
    CarritoRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            CarritoUiState()
        )

    val uiState:
            StateFlow<CarritoUiState> =
        _uiState.asStateFlow()

    init {
        observarCarrito()
    }

    private fun observarCarrito() {

        viewModelScope.launch {

            carritoRepository
                .observeCarrito()
                .collect { items ->

                    val subtotal =
                        items.sumOf {
                            it.subtotal
                        }

                    val delivery = 0.0

                    _uiState.update {

                        it.copy(
                            items = items,
                            subtotal = subtotal,
                            delivery = delivery,
                            total =
                                subtotal +
                                        delivery
                        )
                    }
                }
        }
    }

    fun incrementar(id: Long) {

        viewModelScope.launch {

            carritoRepository
                .incrementar(id)
        }
    }

    fun decrementar(id: Long) {

        viewModelScope.launch {

            carritoRepository
                .decrementar(id)
        }
    }
}