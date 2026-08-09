package com.edu.ucne.parrilladalos2carnales.data.repository.pedido

import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoDao
import com.edu.ucne.parrilladalos2carnales.data.pedido.mapper.toDomain
import com.edu.ucne.parrilladalos2carnales.data.pedido.mapper.toEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido
import com.edu.ucne.parrilladalos2carnales.domain.repository.pedido.PedidoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class PedidoRepositoryImpl @Inject constructor(
    private val pedidoDao: PedidoDao
) : PedidoRepository {
    override suspend fun upsertPedido(pedido: Pedido) {
        val id = pedidoDao.upsertPedido(pedido.toEntity())
        val detalles = pedido.detalles.map { it.copy(idPedido = id.toInt()).toEntity() }
        pedidoDao.upsertDetalles(detalles)
    }

    override suspend fun deletePedido(pedido: Pedido) {
        pedidoDao.deletePedido(pedido.toEntity())
    }

    override fun getPedido(idPedido: Int): Flow<Pedido?> {
        return pedidoDao.getPedido(idPedido).flatMapLatest { pedidoEntity ->
            if (pedidoEntity == null) flowOf(null)
            else {
                pedidoDao.getDetallesPorPedido(idPedido).map { detallesEntities ->
                    pedidoEntity.toDomain(detallesEntities.map { it.toDomain() })
                }
            }
        }
    }

    override fun getPedidos(): Flow<List<Pedido>> {
        return pedidoDao.getPedidos().flatMapLatest { entities ->
            if (entities.isEmpty()) flowOf(emptyList())
            else {
                val flows = entities.map { entity ->
                    pedidoDao.getDetallesPorPedido(entity.idPedido).map { detallesEntities ->
                        entity.toDomain(detallesEntities.map { it.toDomain() })
                    }
                }
                combine(flows) { it.toList() }
            }
        }
    }

    override fun getPedidosPorFecha(fecha: String): Flow<List<Pedido>> {
        return pedidoDao.getPedidosPorFecha(fecha).flatMapLatest { entities ->
            if (entities.isEmpty()) flowOf(emptyList())
            else {
                val flows = entities.map { entity ->
                    pedidoDao.getDetallesPorPedido(entity.idPedido).map { detallesEntities ->
                        entity.toDomain(detallesEntities.map { it.toDomain() })
                    }
                }
                combine(flows) { it.toList() }
            }
        }
    }

    override fun getCarrito(): Flow<Pedido?> {
        return pedidoDao.getCarrito().flatMapLatest { pedidoEntity ->
            if (pedidoEntity == null) flowOf(null)
            else {
                pedidoDao.getDetallesPorPedido(pedidoEntity.idPedido).map { detallesEntities ->
                    pedidoEntity.toDomain(detallesEntities.map { it.toDomain() })
                }
            }
        }
    }

    override suspend fun deleteDetalle(idDetalle: Int) {
        pedidoDao.deleteDetalle(idDetalle)
    }
}
