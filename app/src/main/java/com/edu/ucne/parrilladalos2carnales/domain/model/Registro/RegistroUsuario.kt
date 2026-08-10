package com.edu.ucne.parrilladalos2carnales.domain.model.Registro

import com.edu.ucne.parrilladalos2carnales.domain.model.usuario.Rol

data class RegistroUsuario(
    val nombreUsuario: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val telefono: String = "",
    val calle: String = "",
    val numero: String = "",
    val ciudad: String = "",
    val codigoPostal: String = "",
    val rol: Rol = Rol.CLIENTE
)
