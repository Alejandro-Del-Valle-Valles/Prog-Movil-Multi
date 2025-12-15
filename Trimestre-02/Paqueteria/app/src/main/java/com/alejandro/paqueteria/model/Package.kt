package com.alejandro.paqueteria.model

class Package(
    sender: String, destination: String, val dimensions: Array<Float>,
    val weight: Float, insured: Boolean
) : Mail(sender, destination, insured) {

    companion object {
        val MIN_WIDTH = 10
        val MIN_HEIGHT = 5
        val MIN_LONG = 10
        val MAX_WIDTH = 200
        val MAX_HEIGHT = 150
        val MAX_LONG = 300

        val MAX_WEIGHT = 500
    }

    init {
        require(dimensions.size == 3) {
            "El paquete debe tener 3 valores de dimensión."
        }
        require(dimensions[0] >= MIN_WIDTH || dimensions[0] <= MAX_WIDTH) {
            "El paquete no puede ser inferior a $MIN_WIDTH ni superior a $MAX_WIDTH de ancho."
        }
        require(dimensions[1] >= MIN_HEIGHT || dimensions[1] <= MAX_HEIGHT) {
            "El paquete no puede ser inferior a $MIN_HEIGHT ni superior a $MAX_HEIGHT de alto."
        }
        require(dimensions[2] >= MIN_LONG || dimensions[2] <= MAX_LONG) {
            "El paquete no puede ser inferior a $MIN_LONG ni superior a $MAX_LONG de largo."
        }
        require(weight >= 0 || weight <= MAX_WEIGHT) {
            "El paquete no puede tener peso negativo ni pesar más de ${MAX_WEIGHT}Kg."
        }
    }

}