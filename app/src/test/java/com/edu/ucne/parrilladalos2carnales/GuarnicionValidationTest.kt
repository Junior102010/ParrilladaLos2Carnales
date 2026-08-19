package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.guarnicion.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GuarnicionValidationTest {

    @Test
    fun `validateNombreGuarnicion con nombre vacio retorna error`() {
        val result = validateNombreGuarnicion("")
        assertFalse(result.isValid)
        assertEquals("El nombre no puede estar vacío", result.errorMessage)
    }

    @Test
    fun `validateNombreGuarnicion con nombre valido retorna true`() {
        val result = validateNombreGuarnicion("Papas Fritas")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateDescripcionGuarnicion con descripcion vacia retorna error`() {
        val result = validateDescripcionGuarnicion("")
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePrecioGuarnicion con precio negativo retorna error`() {
        val result = validatePrecioGuarnicion(-5.0)
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePrecioGuarnicion con precio valido retorna true`() {
        val result = validatePrecioGuarnicion(50.0)
        assertTrue(result.isValid)
    }

    @Test
    fun `validateCantidadGuarnicion con cantidad negativa retorna error`() {
        val result = validateCantidadGuarnicion(-1.0)
        assertFalse(result.isValid)
    }

    @Test
    fun `validateCategoriaGuarnicion con categoria vacia retorna error`() {
        val result = validateCategoriaGuarnicion("")
        assertFalse(result.isValid)
    }
}
