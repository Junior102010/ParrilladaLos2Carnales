package com.edu.ucne.parrilladalos2carnales.domain.model.login

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val correo: String = "",
    val direccion: Direccion = Direccion()
)