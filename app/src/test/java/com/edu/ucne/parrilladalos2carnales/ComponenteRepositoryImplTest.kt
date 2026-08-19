package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteDao
import com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente.ComponenteRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Componente
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ComponenteRepositoryImplTest {

    private lateinit var repository: ComponenteRepositoryImpl
    private val componenteDao: ComponenteDao = mockk()

    @Before
    fun setup() {
        repository = ComponenteRepositoryImpl(componenteDao)
    }

    @Test
    fun `upsertComponente llama al DAO y retorna el ID`() = runTest {
        val componente = Componente(
            idComponente = 1,
            nombreComponente = "Pimienta",
            descripcionComponente = "Negra",
            precioComponente = 0.0,
            categoriaComponente = "Especia",
            cantidadComponente = 100.0,
            disponible = true,
            coccion = ""
        )
        coEvery { componenteDao.upsertComponente(any()) } returns 1L

        val result = repository.upsertComponente(componente)

        assertEquals(1, result)
        coVerify { componenteDao.upsertComponente(any()) }
    }

    @Test
    fun `deleteComponente llama al DAO deleteById`() = runTest {
        coEvery { componenteDao.deleteComponenteById(any()) } returns Unit

        repository.deleteComponente(1)

        coVerify { componenteDao.deleteComponenteById(1) }
    }
}
