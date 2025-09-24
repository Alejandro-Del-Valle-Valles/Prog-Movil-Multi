package com.alejandro.calculadora.enums

enum class Operators(val precedence: Int = 0) {
    PLUS(1),
    MINUS(1),
    MULTIPLY(2),
    DIVIDE(2),
    SQUARE(2)
}