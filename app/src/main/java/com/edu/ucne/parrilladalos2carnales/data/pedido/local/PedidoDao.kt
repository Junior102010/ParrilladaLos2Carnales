package com.edu.ucne.parrilladalos2carnales.data.pedido.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PedidoDao {
    @Upsert
    suspend fun upsertPedido(pedido: PedidoEntity): Long

    @Delete
    suspend fun deletePedido(pedido: PedidoEntity)

    @Query("SELECT * FROM Pedidos WHERE idPedido = :idPedido")
    fun getPedido(idPedido: Int): Flow<PedidoEntity?>

    @Query("SELECT * FROM Pedidos")
    fun getPedidos(): Flow<List<PedidoEntity>>

    @Query("SELECT * FROM Pedidos WHERE fecha = :fecha")
    fun getPedidosPorFecha(fecha: String): Flow<List<PedidoEntity>>

    @Upsert
    suspend fun upsertDetalles(detalles: List<DetallePedidoEntity>)

    @Query("SELECT * FROM DetallesPedido WHERE idPedido = :idPedido")
    fun getDetallesPorPedido(idPedido: Int): Flow<List<DetallePedidoEntity>>

    @Query("SELECT * FROM Pedidos WHERE estado = 'PENDIENTE' LIMIT 1")
    fun getCarrito(): Flow<PedidoEntity?>

    @Query("DELETE FROM DetallesPedido WHERE idDetalle = :idDetalle")
    suspend fun deleteDetalle(idDetalle: Int)
}
