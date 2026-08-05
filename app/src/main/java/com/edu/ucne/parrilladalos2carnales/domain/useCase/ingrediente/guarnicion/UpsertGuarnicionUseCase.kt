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

        return runCatching { repository.upsertGuarnicion(guarnicion) }
    }
}