package com.edu.ucne.parrilladalos2carnales.domain.repository.ingrediente

import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import kotlinx.coroutines.flow.Flow

interface GuarnicionRepository {

    suspend fun upsertGuarnicion(guarnicion: Guarnicion) : Int

    suspend fun observeGuarnicion() : Flow<List<Guarnicion>>

    suspend fun deleteGuarnicion(id: Int)

    fun getGuarnicion(idGuarnicion: Int): Flow<Guarnicion?>
    fun getGuarniciones(): Flow<List<Guarnicion>>

}