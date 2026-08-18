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
        val precioResult = validatePrecioComponente(componente.precioComponente, componente.categoriaComponente)
        val categoriaResult = validateCategoriaComponente(componente.categoriaComponente)
        val coccionResult = validateCoccion(componente.categoriaComponente, componente.coccion ?: "")

        if (!nombreResult.isValid){
            return Result.failure(IllegalArgumentException(nombreResult.errorMessage))
        }
        if (!descripcionResult.isValid){
            return Result.failure(IllegalArgumentException(descripcionResult.errorMessage))
        }
        if (!cantidadResult.isValid){
            return Result.failure(IllegalArgumentException(cantidadResult.errorMessage))
        }
        if (!precioResult.isValid){
            return Result.failure(IllegalArgumentException(precioResult.errorMessage))
        }
        if (!categoriaResult.isValid){
            return Result.failure(IllegalArgumentException(categoriaResult.errorMessage))
        }
        if (!coccionResult.isValid){
            return Result.failure(IllegalArgumentException(coccionResult.errorMessage))
        }

        return runCatching { repository.upsertComponente(componente) }
    }
}
