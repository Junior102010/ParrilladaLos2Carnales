package com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import javax.inject.Inject



class UpsertGuarnicionUseCase @Inject constructor(
    private val repository: GuarnicionRepository
){
    suspend operator fun invoke(guarnicion: Guarnicion) : Result<Int> {

        val nombreResult = validateNombreGuarnicion(guarnicion.nombreGuarnicion)
        val descripcionResult = validateDescripcionGuarnicion(guarnicion.descripcionGuarnicion)
        val cantidadResult = validateCantidadGuarnicion(guarnicion.cantidadGuarnicion)
        val precioResult = validatePrecioGuarnicion(guarnicion.precioGuarnicion)
        val categoriaResult = validateCategoriaGuarnicion(guarnicion.categoria)

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

        return runCatching { repository.upsertGuarnicion(guarnicion) }
    }
}
