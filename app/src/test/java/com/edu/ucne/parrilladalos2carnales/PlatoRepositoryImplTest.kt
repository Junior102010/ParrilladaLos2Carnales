package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoDao
import com.edu.ucne.parrilladalos2carnales.data.repository.plato.PlatoRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class PlatoRepositoryImplTest {

    private lateinit var repository: PlatoRepositoryImpl
    private val platoDao: PlatoDao = mockk()

    @Before
    fun setup() {
        repository = PlatoRepositoryImpl(platoDao)
    }

    @Test
    fun `upsertPlato llama al DAO save`() = runTest {
        val plato = Plato(
            idPlato = 1,
            nombre = "Parrillada",
            descripcion = "Varios",
            precio = 500.0,
            imagenUrl = "",
            idCategoria = 1,
            disponible = true
        )
        coEvery { platoDao.save(any()) } returns Unit

        repository.upsertPlato(plato)

        coVerify { platoDao.save(any()) }
    }

    @Test
    fun `deletePlato llama al DAO delete`() = runTest {
        val plato = Plato(
            idPlato = 1,
            nombre = "Parrillada",
            descripcion = "Varios",
            precio = 500.0,
            imagenUrl = "",
            idCategoria = 1,
            disponible = true
        )
        coEvery { platoDao.delete(any()) } returns Unit

        repository.deletePlato(plato)

        coVerify { platoDao.delete(any()) }
    }
}
