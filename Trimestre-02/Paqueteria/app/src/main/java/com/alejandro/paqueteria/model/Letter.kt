package com.alejandro.paqueteria.model

import com.alejandro.paqueteria.enums.Size

class Letter(sender: String, destination: String, val size: Size)
    : Mail(sender, destination, true) {
}