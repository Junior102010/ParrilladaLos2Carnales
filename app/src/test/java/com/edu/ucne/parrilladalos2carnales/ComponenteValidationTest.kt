package com.edu.ucne.parrilladalos2carnales

import com.edu.ucne.parrilladalos2carnales.domain.useCase.ingrediente.componente.*
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ComponenteValidationTest {

    @Test
    fun `validateNombreComponente con nombre corto retorna error`() {
        val result = validateNombreComponente("A")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateNombreComponente con nombre valido retorna true`() {
        val result = validateNombreComponente("Sal")
        assertTrue(result.isValid)
    }

    @Test
    fun `validatePrecioComponente categoria Coccion con precio mayor a 0 retorna error`() {
        val result = validatePrecioComponente(10.0, "Coccion")
        assertFalse(result.isValid)
    }

    @Test
    fun `validatePrecioComponente categoria Coccion con precio 0 retorna true`() {
        val result = validatePrecioComponente(0.0, "Coccion")
        assertTrue(result.isValid)
    }

    @Test
    fun `validateCoccion categoria Coccion con valor vacio retorna error`() {
        val result = validateCoccion("Coccion", "")
        assertFalse(result.isValid)
    }

    @Test
    fun `validateCoccion categoria diferente a Coccion con valor vacio retorna true`() {
        val result = validateCoccion("Extra", "")
        assertTrue(result.isValid)
    }
}
