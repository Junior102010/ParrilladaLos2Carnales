package com.edu.ucne.parrilladalos2carnales

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoDao
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoEntity
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
class PedidoDaoTest {
    private lateinit var pedidoDao: PedidoDao
    private lateinit var db: ParrilladaDb

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, ParrilladaDb::class.java
        ).build()
        pedidoDao = db.pedidoDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetPedido() = runBlocking {
        val pedido = PedidoEntity(
            idPedido = 1,
            usuarioUid = "user123",
            fecha = "2023-10-27",
            total = 1000.0,
            estado = "Pendiente"
        )
        pedidoDao.upsertPedido(pedido)
        val allPedidos = pedidoDao.getPedidos().first()
        assertEquals(allPedidos[0].usuarioUid, "user123")
    }
}
