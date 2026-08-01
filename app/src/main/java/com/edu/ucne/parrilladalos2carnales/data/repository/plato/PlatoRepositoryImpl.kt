package com.edu.ucne.parrilladalos2carnales.data.repository.plato

import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoDao
import com.edu.ucne.parrilladalos2carnales.data.plato.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.plato.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import com.edu.ucne.parrilladalos2carnales.domain.repository.plato.PlatoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PlatoRepositoryImpl @Inject constructor(
    private val platoDao: PlatoDao

) : PlatoRepository {

    override suspend fun upsertPlato(plato: Plato) {
        platoDao.save(plato.toEntity())
    }

    override suspend fun deletePlato(plato: Plato) {
        platoDao.delete(plato.toEntity())
    }

    override fun getPlato(idPlato: Int): Flow<Plato?> {
        return platoDao.getPlato(idPlato).map { it?.toDomain() }
    }

    override fun getPlatos(): Flow<List<Plato>> {
        return platoDao.getAll().map { entidades ->
            entidades.map { it.toDomain() }
        }
    }
}