package com.edu.ucne.parrilladalos2carnales.domain.useCase.oferta

import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.repository.oferta.OfertaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOfertasUseCase @Inject constructor(
    private val ofertaRepository: OfertaRepository
) {
    operator fun invoke(): Flow<List<Oferta>> {
        return ofertaRepository.getOfertas()
    }
}