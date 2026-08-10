package com.edu.ucne.parrilladalos2carnales.domain.repository.oferta

import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import kotlinx.coroutines.flow.Flow

interface OfertaRepository {
    suspend fun upsertOferta(oferta: Oferta)
    suspend fun deleteOferta(oferta: Oferta)
    fun getOferta(idOferta: Int): Flow<Oferta?>
    fun getOfertas(): Flow<List<Oferta>>
}
