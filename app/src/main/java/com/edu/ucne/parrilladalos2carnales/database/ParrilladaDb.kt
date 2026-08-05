package com.edu.ucne.parrilladalos2carnales.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaDao
import com.edu.ucne.parrilladalos2carnales.data.categoria.local.CategoriaEntity
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.componente.local.ComponenteEntity
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionDao
import com.edu.ucne.parrilladalos2carnales.data.ingrediente.guarnicion.local.GuarnicionEntity
import com.edu.ucne.parrilladalos2carnales.data.oferta.local.OfertaDao
import com.edu.ucne.parrilladalos2carnales.data.oferta.local.OfertaEntity
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.DetallePedidoEntity
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoDao
import com.edu.ucne.parrilladalos2carnales.data.pedido.local.PedidoEntity
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoDao
import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoEntity

@Database(
    entities = [
        PlatoEntity::class,
        CategoriaEntity::class,
        OfertaEntity::class,
        ComponenteEntity::class,
        GuarnicionEntity::class,
        PedidoEntity::class,
        DetallePedidoEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class ParrilladaDb : RoomDatabase() {
    abstract fun platoDao(): PlatoDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun ofertaDao(): OfertaDao

    abstract fun componenteDao(): ComponenteDao
    abstract fun guarnicionDao(): GuarnicionDao

    abstract fun pedidoDao(): PedidoDao
}