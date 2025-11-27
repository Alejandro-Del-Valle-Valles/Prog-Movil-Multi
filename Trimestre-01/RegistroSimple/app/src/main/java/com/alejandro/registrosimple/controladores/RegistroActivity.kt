package com.alejandro.registrosimple.controladores

import com.alejandro.registrosimple.extensiones.tryParseToLocalDate
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.registrosimple.databinding.ActivityRegistroBinding
import com.alejandro.registrosimple.modelo.Persona
import com.alejandro.registrosimple.repositorio.PersonaRepositorio
import java.time.LocalDate

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btSalir.setOnClickListener {
            finish()
        }

        binding.btRegistrar.setOnClickListener {
            if(comprobarDatos()) {
                val persona = crearPersona()
                addPersonaRepositorio(persona)
                intent = Intent(this, RegistroFinalizadoActivity::class.java)
                intent.putExtra("Nombre", persona.nombre + " " + persona.apellidos)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun comprobarDatos(): Boolean {
        var sonCorrectos = true
        if(binding.etNombre.text.isNullOrBlank()) {
            sonCorrectos = false
            Toast.makeText(this,
                "El nombre está vacío.", Toast.LENGTH_SHORT).show()
        }
        if(binding.etApellidos.text.isNullOrBlank()) {
            sonCorrectos = false
            Toast.makeText(this,
                "Los apellidos están vacíos.", Toast.LENGTH_SHORT).show()
        }
        if(binding.etEmail.text.isNullOrBlank()
            || !binding.etEmail.text.contains('@')
            || !binding.etEmail.text.contains('.')) {
            sonCorrectos = false
            Toast.makeText(this,
                "El email está vacío o no contiene un dominio (@ejemplo.com)",
                Toast.LENGTH_SHORT).show()
        }
        if(binding.etContrsena.text.isNullOrBlank()) {
            sonCorrectos = false
            Toast.makeText(this,
                "La contraseña está vacía.", Toast.LENGTH_SHORT).show()
        }
        if(binding.etFecha.text.isNullOrBlank()
            || binding.etFecha.text.toString().tryParseToLocalDate() == null
            || binding.etFecha.text.toString().tryParseToLocalDate()!! > LocalDate.now()
        ) {
            sonCorrectos = false
            Toast.makeText(this,
                "La fecha está vacía, es posterior a  o no tiene el formato correcto (dd/MM/yyyy).",
                Toast.LENGTH_SHORT).show()
        }
        if(!binding.cbAcepta.isChecked) {
            sonCorrectos = false
            Toast.makeText(this,
                "No has aceptado los términos y condiciones", Toast.LENGTH_SHORT).show()
        }
        return sonCorrectos
    }

    private fun crearPersona(): Persona {
        val fecha = binding.etFecha.text.toString().tryParseToLocalDate() ?: LocalDate.now()

        return Persona(binding.etNombre.text.toString(),
            binding.etApellidos.text.toString(),
            binding.etEmail.text.toString(),
            binding.etContrsena.text.toString(),
            fecha)
    }

    private fun addPersonaRepositorio(persona: Persona) {
        PersonaRepositorio.persona = persona
    }
}