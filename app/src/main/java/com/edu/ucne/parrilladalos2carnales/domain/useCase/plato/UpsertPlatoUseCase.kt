package com.edu.ucne.parrilladalos2carnales.domain.useCase.plato

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import javax.inject.Inject

class UpsertPlatoUseCase @Inject constructor(
    private val platoRepository: PlatoRepository
) {
    suspend operator fun invoke(plato: Plato): Result<Unit> {
        if (plato.nombre.isBlank()) {
            return Result.failure(IllegalArgumentException("El nombre del plato no puede estar vacío"))
        }
        if (plato.precio <= 0.0) {
            return Result.failure(IllegalArgumentException("El precio debe ser mayor a cero"))
        }
        try {
            platoRepository.upsertPlato(plato)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}