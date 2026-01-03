package com.alejandro.mundomagico.model

import java.math.BigDecimal

class CreateVaritaDTO(val madera: String, val nucleo: String, val longitud: Float) {

    companion object {
        val MIN_LENGTH_WOOD: Int = 5
        val MIN_LENGTH_CORE: Int = 5
        val MIN_LENGTH: Float = 0f
    }
}