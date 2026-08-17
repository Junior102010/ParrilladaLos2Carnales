package com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta

import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.repository.oferta.OfertaRepository
import javax.inject.Inject

class UpsertOfertaUseCase @Inject constructor(
    private val ofertaRepository: OfertaRepository
) {
    suspend operator fun invoke(oferta: Oferta): Result<Unit> {
        val tituloResult = validateTituloOferta(oferta.tituloOferta)
        val descuentoResult = validateDescuentoOferta(oferta.descuento)

        if (!tituloResult.isValid) {
            return Result.failure(IllegalArgumentException(tituloResult.errorMessage))
        }
        if (!descuentoResult.isValid) {
            return Result.failure(IllegalArgumentException(descuentoResult.errorMessage))
        }

        return runCatching { ofertaRepository.upsertOferta(oferta) }
    }
}
