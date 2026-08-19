package com.edu.ucne.parrilladalos2carnales

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteEntity
import com.edu.ucne.parrilladalos2carnales.database.ParrilladaDb
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ComponenteDaoTest {
    private lateinit var componenteDao: ComponenteDao
    private lateinit var db: ParrilladaDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ParrilladaDb::class.java
        ).build()
        componenteDao = db.componenteDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetComponente() = runBlocking {
        val componente = ComponenteEntity(
            idComponente = 1,
            nombreComponente = "Pimienta",
            descripcionComponente = "Negra",
            precioComponente = 0.0,
            categoriaComponente = "Especia",
            cantidadComponente = 100.0,
            disponible = true
        )
        componenteDao.upsertComponente(componente)
        val loaded = componenteDao.getComponente(1)
        assertEquals(loaded?.nombreComponente, "Pimienta")
    }
}
