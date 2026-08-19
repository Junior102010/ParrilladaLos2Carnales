package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoDao
import com.edu.ucne.parrilladalos2carnales.data.repository.pedido.PedidoRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PedidoRepositoryImplTest {

    private lateinit var repository: PedidoRepositoryImpl
    private val pedidoDao: PedidoDao = mockk()

    @Before
    fun setup() {
        repository = PedidoRepositoryImpl(pedidoDao)
    }

    @Test
    fun `upsertPedido llama al DAO upsertPedido y upsertDetalles`() = runTest {
        val pedido = Pedido(
            idPedido = 1,
            usuarioUid = "user123",
            fecha = "2023-10-27",
            total = 1000.0,
            estado = EstadoPedido.RECIBIDO,
            detalles = emptyList()
        )
        coEvery { pedidoDao.upsertPedido(any()) } returns 1L
        coEvery { pedidoDao.upsertDetalles(any()) } returns Unit

        val result = repository.upsertPedido(pedido)

        assertEquals(1, result)
        coVerify { pedidoDao.upsertPedido(any()) }
        coVerify { pedidoDao.upsertDetalles(any()) }
    }

    @Test
    fun `deletePedido llama al DAO deletePedido`() = runTest {
        val pedido = Pedido(
            idPedido = 1,
            usuarioUid = "user123",
            fecha = "2023-10-27",
            total = 1000.0,
            estado = EstadoPedido.RECIBIDO,
            detalles = emptyList()
        )
        coEvery { pedidoDao.deletePedido(any()) } returns Unit

        repository.deletePedido(pedido)

        coVerify { pedidoDao.deletePedido(any()) }
    }
}
