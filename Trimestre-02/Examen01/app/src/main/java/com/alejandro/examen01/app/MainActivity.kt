package com.alejandro.examen01.app

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.examen01.databinding.ActivityMainBinding
import com.alejandro.examen01.helper.ConfirmDialog
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val REQUEST_KEY_CODIGO = "Confirmar"
    private val MIME_TYPE_TEXT = "text/plain"
    private val crearLanzadorFicheros = registerForActivityResult(
        ActivityResultContracts.CreateDocument(MIME_TYPE_TEXT)) { uri ->
        if(uri != null)
            crearFichero(uri)
        else
            Toast.makeText(this, "Se canceló el guardado", Toast.LENGTH_SHORT)
                .show()

    }

    private val leerLanzadorFichero = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri ->
        if(uri != null) leerContenidoFichero(uri)
        else Toast.makeText(this, "Se canceló la selección", Toast.LENGTH_SHORT)
            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)


        supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY_CODIGO,
            this
        ) { requestKey, bundle ->
            when (bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    //TODO: Reproducir sonido
                }
            }
        }

        binding.btAbrirFichero.setOnClickListener {
            leerLanzadorFichero.launch("text/*")
        }

        binding.btGuardarFichero.setOnClickListener {
            if (!binding.etTexto.text.isEmpty()) crearLanzadorFicheros.launch("text/*")
        }
    }

    private fun crearFichero(uri: Uri) {
        val contenido = binding.etTexto.text.toString().trim()
        try {
            contentResolver.openOutputStream(uri)?.use { salida ->
                salida.write(contenido.toByteArray())
                Toast.makeText(this, "Arhcivo creado en $uri", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Error al crear el fichero.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun leerContenidoFichero(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { lectura ->
                val lector = BufferedReader(InputStreamReader(lectura))
                val contenido: String? = lector.readText()

                if(contenido != null) {
                    if (contenido.length < 50)
                        Toast.makeText(this, contenido, Toast.LENGTH_LONG)
                            .show()
                    else if (contenido.length >= 50 && contenido.length <= 100)
                        ConfirmDialog.newInstance(
                            REQUEST_KEY_CODIGO,
                            "Contenido del fichero",
                            contenido,
                            "Aceptar"
                        )
                    else
                        //TODO Crear Noti
                        Toast.makeText(this, "Esto se debe mostrar en una Noti", Toast.LENGTH_LONG)
                            .show()
                } else Toast.makeText(this, "Contenido vacío", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Error al leer el fichero", Toast.LENGTH_SHORT)
                .show()
        }
    }

}