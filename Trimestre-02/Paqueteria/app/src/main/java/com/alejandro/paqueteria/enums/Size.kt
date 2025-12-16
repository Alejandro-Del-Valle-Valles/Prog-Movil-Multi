package com.alejandro.paqueteria.enums

enum class Size(val reference: String, val width: Int, val height: Int) {
    SMALL("Pequeño", 10, 17),
    MEDIUM("Mediano", 15, 25),
    BIG("Grande", 25, 45);

    override fun toString(): String {
        return reference
    }
}