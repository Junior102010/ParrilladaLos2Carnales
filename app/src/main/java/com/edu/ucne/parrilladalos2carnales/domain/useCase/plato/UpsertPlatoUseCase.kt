package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import javax.inject.Inject

class UpsertPlatoUseCase @Inject constructor(
    private val platoRepository: PlatoRepository
) {
    suspend operator fun invoke(plato: Plato): Result<Unit> {
        val nombreResult = validateNombrePlato(plato.nombre)
        val precioResult = validatePrecioPlato(plato.precio)

        if (!nombreResult.isValid) {
            return Result.failure(IllegalArgumentException(nombreResult.errorMessage))
        }
        if (!precioResult.isValid) {
            return Result.failure(IllegalArgumentException(precioResult.errorMessage))
        }

        return runCatching { platoRepository.upsertPlato(plato) }
    }
}
