package com.edu.ucne.parrilladalos2carnales.domain.repository.pedido


import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import kotlinx.coroutines.flow.Flow

interface PedidoRepository {
    suspend fun upsertPedido(pedido: Pedido)

    suspend fun deletePedido(pedido: Pedido)

    fun getPedido(idPedido: Int): Flow<Pedido?>
    
    fun getPedidos(): Flow<List<Pedido>>

    fun getPedidosPorFecha(fecha: String): Flow<List<Pedido>>

    fun getCarrito(): Flow<Pedido?>

    suspend fun deleteDetalle(idDetalle: Int)
}