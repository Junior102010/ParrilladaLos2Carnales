package com.edu.ucne.parrilladalos2carnales

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionEntity
import com.edu.ucne.parrilladalos2carnales.database.ParrilladaDb
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class GuarnicionDaoTest {
    private lateinit var guarnicionDao: GuarnicionDao
    private lateinit var db: ParrilladaDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ParrilladaDb::class.java
        ).build()
        guarnicionDao = db.guarnicionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetGuarnicion() = runBlocking {
        val guarnicion = GuarnicionEntity(
            idGuarnicion = 1,
            nombreGuarnicion = "Papas",
            descripcionGuarnicion = "Fritas",
            cantidad = 10.0,
            precioGuarnicion = 50.0,
            disponible = true,
            categoria = "Frituras"
        )
        guarnicionDao.save(guarnicion)
        val allGuarniciones = guarnicionDao.getGuarniciones().first()
        assertEquals(allGuarniciones[0].nombreGuarnicion, "Papas")
    }
}
