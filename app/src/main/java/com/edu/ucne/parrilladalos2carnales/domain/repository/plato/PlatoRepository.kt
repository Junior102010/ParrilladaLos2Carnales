package com.edu.ucne.parrilladalos2carnales.domain.repository.plato

import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import kotlinx.coroutines.flow.Flow



interface PlatoRepository {
    suspend fun upsertPlato(plato: Plato)
    suspend fun deletePlato(plato: Plato)
    fun getPlato(idPlato: Int): Flow<Plato?>
    fun getPlatos(): Flow<List<Plato>>
}