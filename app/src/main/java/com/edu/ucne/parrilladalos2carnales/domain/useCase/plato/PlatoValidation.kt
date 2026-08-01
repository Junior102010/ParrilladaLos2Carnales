package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

import javax.inject.Inject

class PlatoValidation @Inject constructor() {
    operator fun invoke(nombre: String, precio: Double): Boolean {
        return nombre.isNotBlank() && precio > 0.0
    }
}