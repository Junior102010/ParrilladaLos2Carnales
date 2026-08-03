package com.edu.ucne.parrilladalos2carnales.data.repository.oferta



import com.edu.ucne.parrilladalos2carnales.data.oferta.local.OfertaDao
import com.edu.ucne.parrilladalos2carnales.data.oferta.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.oferta.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.oferta.Oferta
import com.edu.ucne.parrilladalos2carnales.domain.repository.oferta.OfertaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class OfertaRepositoryImpl @Inject constructor(
    private val ofertaDao: OfertaDao
) : OfertaRepository {
    override suspend fun upsertOferta(oferta: Oferta) {
        ofertaDao.save(oferta.toEntity())
    }

    override suspend fun deleteOferta(oferta: Oferta) {
        ofertaDao.delete(oferta.toEntity())
    }

    override fun getOferta(idOferta: Int): Flow<Oferta?> {
        return ofertaDao.getOferta(idOferta).map { it?.toDomain() }
    }

    override fun getOfertas(): Flow<List<Oferta>> {
        return ofertaDao.getOfertas().map { entities -> entities.map { it.toDomain() } }
    }
}