package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaDao
import com.edu.ucne.parrilladalos2carnales.data.repository.categoria.CategoriaRepositoryImpl
import com.edu.ucne.parrilladalos2carnales.domain.model.categoria.Categoria
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CategoriaRepositoryImplTest {

    private lateinit var repository: CategoriaRepositoryImpl
    private val categoriaDao: CategoriaDao = mockk()

    @Before
    fun setup() {
        repository = CategoriaRepositoryImpl(categoriaDao)
    }

    @Test
    fun `upsertCategoria llama al DAO save`() = runTest {
        val categoria = Categoria(
            idCategoria = 1,
            nombreCategoria = "Carnes",
            descripcionCategoria = "Todo tipo de carnes",
            imagenUrl = ""
        )
        coEvery { categoriaDao.save(any()) } returns Unit

        repository.upsertCategoria(categoria)

        coVerify { categoriaDao.save(any()) }
    }
}
