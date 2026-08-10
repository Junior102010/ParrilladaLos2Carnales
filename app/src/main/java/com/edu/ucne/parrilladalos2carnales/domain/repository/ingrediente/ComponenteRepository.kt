package com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import kotlinx.coroutines.flow.Flow

interface ComponenteRepository {
    suspend fun upsertComponente(componente: Componente): Int
    suspend fun deleteComponente(id: Int)
    suspend fun getComponente(id: Int): Componente?
    fun getComponentes(): Flow<List<Componente>>
    suspend fun observeComponente(): Flow<List<Componente>>
}
