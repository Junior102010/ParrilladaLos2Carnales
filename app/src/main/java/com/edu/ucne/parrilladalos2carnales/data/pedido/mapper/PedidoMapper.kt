package com.edu.ucne.parrilladalos2carnales.data.pedido.mapper

import com.edu.ucne.parrilladalos2carnales.data.pedido.local.DetallePedidoEntity
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.DetallePedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

fun Pedido.toEntity() = PedidoEntity(
    idPedido = idPedido,
    idUsuario = idUsuario,
    clienteNombre = clienteNombre,
    fecha = fecha,
    fechaMillis = fechaMillis,
    total = total,
    estado = estado.name
)

fun PedidoEntity.toDomain(detalles: List<DetallePedido>) = Pedido(
    idPedido = idPedido,
    idUsuario = idUsuario,
    clienteNombre = clienteNombre,
    fecha = fecha,
    fechaMillis = fechaMillis,
    total = total,
    estado = try { EstadoPedido.valueOf(estado) } catch (e: Exception) { EstadoPedido.PENDIENTE },
    detalles = detalles
)

fun DetallePedido.toEntity() = DetallePedidoEntity(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal
)

fun DetallePedidoEntity.toDomain() = DetallePedido(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal
)
