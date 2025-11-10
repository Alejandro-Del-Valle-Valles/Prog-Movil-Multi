package com.alejandro.registrosimple.modelo

import java.time.LocalDate

class Persona(
    var nombre: String, var apellidos: String, var email: String,
    var contrasena: String, var fechaNacimiento: LocalDate
)