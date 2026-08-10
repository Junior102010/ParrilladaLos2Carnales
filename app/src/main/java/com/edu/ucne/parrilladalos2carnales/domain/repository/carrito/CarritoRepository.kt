package com.edu.ucne.parrilladalos2carnales.domain.repository.carrito


import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import kotlinx.coroutines.flow.Flow

interface CarritoRepository {

    fun observeCarrito(): Flow<List<CarritoItem>>

    suspend fun agregar(item: CarritoItem)

    suspend fun incrementar(idCarritoItem: Long)

    suspend fun decrementar(idCarritoItem: Long)

    suspend fun eliminar(idCarritoItem: Long)

    suspend fun vaciar()
}