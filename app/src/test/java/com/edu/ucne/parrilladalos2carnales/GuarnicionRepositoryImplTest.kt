package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionEntity
import com.edu.ucne.parrilladalos2carnales.data.repository.ingrediente.GuarnicionRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.model.ingrediente.Guarnicion
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GuarnicionRepositoryImplTest {

    private lateinit var repository: GuarnicionRepositoryImpl
    private val guarnicionDao: GuarnicionDao = mockk()

    @Before
    fun setup() {
        repository = GuarnicionRepositoryImpl(guarnicionDao)
    }

    @Test
    fun `upsertGuarnicion llama al DAO y retorna el ID`() = runTest {
        val guarnicion = Guarnicion(
            idGuarnicion = 1,
            nombreGuarnicion = "Papas",
            descripcionGuarnicion = "Fritas",
            precioGuarnicion = 50.0,
            cantidadGuarnicion = 10.0,
            categoria = "Frituras",
            disponible = true
        )
        coEvery { guarnicionDao.save(any()) } returns 1L

        val result = repository.upsertGuarnicion(guarnicion)

        assertEquals(1, result)
        coVerify { guarnicionDao.save(any()) }
    }

    @Test
    fun `deleteGuarnicion llama al DAO delete`() = runTest {
        coEvery { guarnicionDao.delete(any()) } returns Unit

        repository.deleteGuarnicion(1)

        coVerify { guarnicionDao.delete(any()) }
    }
}
