package com.edu.ucne.parrilladalos2carnales.data.pedido.mapper

import com.edu.ucne.parrilladalos2carnales.data.pedido.local.DetallePedidoEntity
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.DetallePedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.EstadoPedido
import com.edu.ucne.parrilladalos2carnales.domain.model.pedido.Pedido

fun Pedido.toEntity() = PedidoEntity(
    idPedido = idPedido,
    idUsuario = idUsuario,
    usuarioUid = usuarioUid,
    clienteNombre = clienteNombre,
    fecha = fecha,
    fechaMillis = fechaMillis,
    subtotal = subtotal,
    costoDelivery = costoDelivery,
    total = total,
    tipoEntrega = tipoEntrega,
    direccion = direccion,
    metodoPago = metodoPago,
    tiempoEstimado = tiempoEstimado,
    estado = estado.name
)

fun PedidoEntity.toDomain(detalles: List<DetallePedido>) = Pedido(
    idPedido = idPedido,
    idUsuario = idUsuario,
    usuarioUid = usuarioUid,
    clienteNombre = clienteNombre,
    fecha = fecha,
    fechaMillis = fechaMillis,
    subtotal = subtotal,
    costoDelivery = costoDelivery,
    total = total,
    tipoEntrega = tipoEntrega,
    direccion = direccion,
    metodoPago = metodoPago,
    tiempoEstimado = tiempoEstimado,
    estado = try {
        EstadoPedido.valueOf(estado)
    } catch (_: Exception) {
        EstadoPedido.PENDIENTE
    },
    detalles = detalles
)

fun DetallePedido.toEntity() = DetallePedidoEntity(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
    termino = termino,
    guarnicion = guarnicion,
    salsa = salsa
)

fun DetallePedidoEntity.toDomain() = DetallePedido(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
    termino = termino,
    guarnicion = guarnicion,
    salsa = salsa
)
