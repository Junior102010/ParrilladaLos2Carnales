package com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.ComponenteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ComponenteRepositoryImpl @Inject constructor(
    private val componenteDao: ComponenteDao
) : ComponenteRepository {

    override suspend fun upsertComponente(componente: Componente): Int {
        val id = componenteDao.upsertComponente(componente.toEntity())
        return id.toInt()
    }

    override suspend fun deleteComponente(id: Int) {
        componenteDao.deleteComponenteById(id)
    }

    override suspend fun getComponente(id: Int): Componente? {
        return componenteDao.getComponente(id)?.toDomain()
    }

    override fun getComponentes(): Flow<List<Componente>> {
        return componenteDao.getComponentes().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun observeComponente(): Flow<List<Componente>> {
        return componenteDao.getComponentes().map { lista ->
            lista.map { it.toDomain() }
        }
    }
}
