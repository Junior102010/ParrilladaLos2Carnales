package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import javax.inject.Inject

class UpsertComponenteUseCase @Inject constructor(
    private val repository: ComponenteRepository
){
    suspend operator fun invoke(componente: Componente) : Result<Int> {

        val nombreResult = validateNombreComponente(componente.nombreComponente)
        val descripcionResult = validateDescripcionComponente(componente.descripcionComponente)
        val cantidadResult = validateCantidadComponente(componente.cantidadComponente)
        val precioResult = validatePrecioComponente(componente.precioComponente)

        if (!nombreResult.isValid){
            return Result.failure(IllegalArgumentException(nombreResult.error))
        }
        if (!descripcionResult.isValid){
            return Result.failure(IllegalArgumentException(descripcionResult.error))
        }
        if (!cantidadResult.isValid){
            return Result.failure(IllegalArgumentException(cantidadResult.error))
        }
        if (!precioResult.isValid){
            return Result.failure(IllegalArgumentException(precioResult.error))
        }

        return runCatching { repository.upsertComponente(componente) }
    }
}
