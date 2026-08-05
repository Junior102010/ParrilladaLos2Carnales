package com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface GuarnicionDao {
    @Upsert
    suspend fun save(guarnicion: GuarnicionEntity)

    @Delete
    suspend fun delete(guarnicion: GuarnicionEntity)

    @Query("SELECT * FROM Guarniciones WHERE idGuarnicion = :idGuarnicion")
    fun getGuarnicion(idGuarnicion: Int): Flow<GuarnicionEntity?>

    @Query("SELECT * FROM Guarniciones")
    fun getGuarniciones(): Flow<List<GuarnicionEntity>>
}
