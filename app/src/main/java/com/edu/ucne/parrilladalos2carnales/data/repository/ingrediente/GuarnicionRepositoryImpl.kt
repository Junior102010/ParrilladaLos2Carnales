package com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionEntity
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente.GuarnicionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GuarnicionRepositoryImpl @Inject constructor(
    private val guarnicionDao: GuarnicionDao
) : GuarnicionRepository {

    override suspend fun upsertGuarnicion(guarnicion: Guarnicion): Int {
        guarnicionDao.save(guarnicion.toEntity())
        return guarnicion.idGuarnicion
    }

    override suspend fun deleteGuarnicion(id: Int) {
        guarnicionDao.delete(GuarnicionEntity(idGuarnicion = id))
    }

    override suspend fun getGuarnicion(id: Int): Guarnicion? {
        return guarnicionDao.getGuarnicion(id).firstOrNull()?.toDomain()
    }

    override fun getGuarniciones(): Flow<List<Guarnicion>> {
        return guarnicionDao.getGuarniciones().map { lista ->
            lista.map { it.toDomain() }
        }
    }

    override suspend fun observeGuarnicion(): Flow<List<Guarnicion>> {
        return guarnicionDao.getGuarniciones().map { lista ->
            lista.map { it.toDomain() }
        }
    }
}

