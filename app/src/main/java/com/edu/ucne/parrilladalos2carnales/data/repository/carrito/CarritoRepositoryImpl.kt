package com.edu.ucne.parrilladalos2carnales.data.repository.carrito


import com.edu.ucne.parrilladalos2carnales.domain.model.carrito.CarritoItem
import com.edu.ucne.parrilladalos2carnales.domain.repository.carrito.CarritoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CarritoRepositoryImpl @Inject constructor() : CarritoRepository {

    private val _items =
        MutableStateFlow<List<CarritoItem>>(emptyList())

    override fun observeCarrito(): Flow<List<CarritoItem>> =
        _items.asStateFlow()

    override suspend fun agregar(item: CarritoItem) {

        val actual = _items.value.toMutableList()


        val index = actual.indexOfFirst {

            it.plato.idPlato == item.plato.idPlato &&

                    it.termino?.idComponente ==
                    item.termino?.idComponente &&

                    it.guarnicion?.idGuarnicion ==
                    item.guarnicion?.idGuarnicion &&

                    it.salsa?.idComponente ==
                    item.salsa?.idComponente
        }

        if (index >= 0) {

            val existente = actual[index]

            actual[index] =
                existente.copy(
                    cantidad =
                        existente.cantidad +
                                item.cantidad
                )

        } else {

            actual.add(item)
        }

        _items.value = actual
    }

    override suspend fun incrementar(
        idCarritoItem: Long
    ) {

        _items.value =
            _items.value.map {

                if (
                    it.idCarritoItem ==
                    idCarritoItem
                ) {
                    it.copy(
                        cantidad =
                            it.cantidad + 1
                    )
                } else {
                    it
                }
            }
    }

    override suspend fun decrementar(
        idCarritoItem: Long
    ) {

        val item =
            _items.value.find {
                it.idCarritoItem ==
                        idCarritoItem
            } ?: return

        if (item.cantidad <= 1) {

            eliminar(idCarritoItem)

        } else {

            _items.value =
                _items.value.map {

                    if (
                        it.idCarritoItem ==
                        idCarritoItem
                    ) {

                        it.copy(
                            cantidad =
                                it.cantidad - 1
                        )

                    } else {
                        it
                    }
                }
        }
    }

    override suspend fun eliminar(
        idCarritoItem: Long
    ) {

        _items.value =
            _items.value.filterNot {
                it.idCarritoItem ==
                        idCarritoItem
            }
    }

    override suspend fun vaciar() {
        _items.value = emptyList()
    }
}
