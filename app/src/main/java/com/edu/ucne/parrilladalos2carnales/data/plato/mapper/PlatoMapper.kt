import com.edu.ucne.parrilladalos2carnales.data.plato.local.PlatoEntity
import com.edu.ucne.parrilladalos2carnales.domain.model.plato.Plato

fun PlatoEntity.toDomain(): Plato {
    return Plato(
        idPlato = this.idPlato,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        idCategoria = this.idCategoria,
        disponible = this.disponible
    )
}

fun Plato.toEntity(): PlatoEntity {
    return PlatoEntity(
        idPlato = this.idPlato,
        nombre = this.nombre,
        descripcion = this.descripcion,
        precio = this.precio,
        imagenUrl = this.imagenUrl,
        idCategoria = this.idCategoria,
        disponible = this.disponible
    )
}