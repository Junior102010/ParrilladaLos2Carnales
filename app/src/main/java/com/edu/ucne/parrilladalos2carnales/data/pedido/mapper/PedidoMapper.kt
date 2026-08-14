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
    estado = when (estado) {
        "PENDIENTE" -> EstadoPedido.RECIBIDO
        "EN_PROCESO" -> EstadoPedido.PREPARANDO
        else -> try {
            EstadoPedido.valueOf(estado)
        } catch (_: Exception) {
            EstadoPedido.RECIBIDO
        }
    },
    detalles = detalles
)

fun DetallePedido.toEntity() = DetallePedidoEntity(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    imagenUrl = imagenUrl,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
    termino = termino,
    idTermino = idTermino,
    guarnicion = guarnicion,
    idGuarnicion = idGuarnicion,
    salsa = salsa,
    idSalsa = idSalsa
)

fun DetallePedidoEntity.toDomain() = DetallePedido(
    idDetalle = idDetalle,
    idPedido = idPedido,
    idPlato = idPlato,
    nombrePlato = nombrePlato,
    imagenUrl = imagenUrl,
    cantidad = cantidad,
    precioUnitario = precioUnitario,
    subtotal = subtotal,
    termino = termino,
    idTermino = idTermino,
    guarnicion = guarnicion,
    idGuarnicion = idGuarnicion,
    salsa = salsa,
    idSalsa = idSalsa
)
