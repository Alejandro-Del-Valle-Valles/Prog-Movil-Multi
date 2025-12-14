package com.alejandro.paqueteria.controller

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.paqueteria.databinding.ActivityPackageSenderBinding
import com.alejandro.paqueteria.dialogs.ConfirmationDialog
import com.alejandro.paqueteria.dialogs.WarningDialog
import com.alejandro.paqueteria.model.Package

//TODO: No funciona. Si no se rellenan los datos y se pulsa enviar, la activity se cierra pero la App no.
//TODO: Si se rellena algún dato, pero el resto no, falla la app.
class PackageSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackageSenderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPackageSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val REQUEST_KEY_CODE = "ConfirmationCode"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODE, this) { requestKey,
            bundle ->
                when (bundle.getString(ConfirmationDialog.RESULT_KEY_ACTION)) {
                    ConfirmationDialog.ACTION_POSITIVE -> {
                        //TODO: Lanzar notificación con la info del paquete y que al pulsar lanze el receptionActivity
                    }

                    ConfirmationDialog.ACTION_NEGATIVE -> {
                        //Do Nothing
                        }
                    }
            }

        binding.tvPackageInformation.setText("El paquete debe tener un mínimo de ${Package.MIN_WIDTH} (Largo) por " +
                "${Package.MIN_HEIGHT} (Alto) por ${Package.MIN_LONG} (Profundo); y un máximo de ${Package.MAX_WIDTH} (Largo) por " +
                "${Package.MAX_HEIGHT} (Alto) por ${Package.MAX_LONG} (Profundo). Tampoco puede pesar más de ${Package.MAX_WEIGHT}Kg.")

        binding.btCancelPackage.setOnClickListener {
            finish()
        }

        binding.btSendPackage.setOnClickListener {
            if(checkData()) {
                val newPackage = Package(
                    binding.etSenderPackage.text.toString().trim(),
                    binding.etDestinationPackage.text.toString().trim(),
                    parseSize().filterNotNull().toTypedArray(), //This cannot throw an Exception because it is checked before set it.
                    binding.etWeightPackage.text.toString().toFloat(),
                    binding.cbInsured.isChecked
                )
                //TODO: Mostrar un Dialog con "Confirmar" y "Cancelar"
            } else {
                //TODO: Mostrar un MaterialCardView notificando de que la info no está bien.
            }
        }
    }

    /**
     * Check if all data is correct. If not, shows a Dialog with the incorrect data.
     * @return Boolean, true if all data is correct, false otherwise
     */
    private fun checkData(): Boolean {
        val weight = binding.etWeightPackage.text.toString().toFloat()
        val size = parseSize()

        val isSenderCorrect = !binding.etSenderPackage.text.isNullOrBlank()
        val isDestinationCorrect = !binding.etDestinationPackage.text.isNullOrBlank()
        val areDimensionsCorrect = !size.contains(null) ||
                ((size[0]!! >= Package.MIN_WIDTH && size[0]!! <= Package.MAX_WIDTH)
                        || (size[1]!! >= Package.MIN_HEIGHT && size[1]!! <= Package.MAX_HEIGHT)
                        || (size[2]!! >= Package.MIN_LONG && size[2]!! <= Package.MAX_LONG))
        val isWeightCorrect = weight > 0 && Package.MAX_WEIGHT <= weight
        if(!isSenderCorrect) WarningDialog.newInstance("Remitente vacío", "El remitente no puede estar vacío.")
        if(!isDestinationCorrect) WarningDialog.newInstance("Destinatario vacío", "El destinatario no puede estar vacío.")
        if(areDimensionsCorrect) WarningDialog.newInstance("Dimensiones incorrectas", "Las dimensiones tienen que tener el formato " +
                "largoXanchoXprofundo y no deben superar las medidas estipuladas.")
        if(!isWeightCorrect) WarningDialog.newInstance("Peso no válido", "El peso no puede ser inferior a 0 ni superior a ${Package.MAX_WEIGHT}Kg")
        return  isSenderCorrect && isDestinationCorrect && areDimensionsCorrect && isWeightCorrect
    }

    /**
     * Parse the data from the editText of the sizes to a Array of floats. If the size of the array is different of 3
     * or some value isn't a float, it doesn't parse the sizes and return a Array of nulls
     * @return Array<Float?> size 3
     */
    private fun parseSize(): Array<Float?> {
        val size = arrayOfNulls<Float?>(3)
        try {
            val sizes = binding.etPackageDimensions.text.toString()
                .replace("x", "X").trim().split("X")
            if(sizes.size == 3) {
                for(i in 0 .. sizes.size - 1) {
                    size[i] = sizes[i].toFloat()
                }
            } else throw Exception("No se han introducido las 3 dimensiones separadas por X.")
        } catch (ex: Exception) {
            WarningDialog.newInstance("Error en los datos de tamaño", ex.message!! )
        }
        return size
    }
}