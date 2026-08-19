package com.edu.ucne.parrilladalos2carnales

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoDao
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoEntity
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
class PlatoDaoTest {
    private lateinit var platoDao: PlatoDao
    private lateinit var db: ParrilladaDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ParrilladaDb::class.java
        ).build()
        platoDao = db.platoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPlato() = runBlocking {
        val plato = PlatoEntity(
            idPlato = 1,
            nombre = "Parrillada Mixta",
            descripcion = "Varios tipos de carne",
            precio = 500.0,
            idCategoria = 1,
            imagenUrl = ""
        )
        platoDao.save(plato)
        val allPlatos = platoDao.getPlatos().first()
        assertEquals(allPlatos[0].nombre, "Parrillada Mixta")
    }
}
