package com.alejandro.mundomagico.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alejandro.mundomagico.databinding.ActivityVaritaBinding
import com.alejandro.mundomagico.model.ConfirmDialog
import com.alejandro.mundomagico.model.CreateVaritaDTO
import com.alejandro.mundomagico.service.ApiService
import com.alejandro.mundomagico.service.UrlApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class VaritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVaritaBinding
    private lateinit var service: ApiService
    private val KEY_CONFIRM_EXIT = "KEY_CONFIRM_EXIT"
    private val KEY_CONFIRM_ACTION = "KEY_CONFIRM_ACTION"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRetrofit()

        val idVarita = intent.getIntExtra("id", -1)

        if (idVarita > -1) {
            breakVaritaActions()
        }

        supportFragmentManager.setFragmentResultListener(
            KEY_CONFIRM_EXIT, this) { _, bundle ->
            val result = bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)
            if (result == ConfirmDialog.ACTION_POSITIVE) {
                finish()
            }
        }

        supportFragmentManager.setFragmentResultListener(KEY_CONFIRM_ACTION, this) { _, bundle ->
            val result = bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)
            if (result == ConfirmDialog.ACTION_POSITIVE) {
                if (idVarita > -1) {
                    breakVarita(idVarita)
                } else {
                    createVarita()
                }
            }
        }
        binding.btExitCreateBreakVarita.setOnClickListener {
            showDialog(
                requestKey = KEY_CONFIRM_EXIT,
                title = "Salir",
                message = "¿Quieres salir? Se cancelará la acción."
            )
        }
        binding.btCreateBreak.setOnClickListener {
            if (checkData()) {
                val acction = if (idVarita > -1) "romper" else "crear"
                val mensaje = "¿Estás seguro de que quieres $acction la varita?"

                showDialog(
                    requestKey = KEY_CONFIRM_ACTION,
                    title = "Confirmar acción",
                    message = mensaje
                )
            } else {
                showToast("Por favor, rellena todos los datos correctamente.")
            }
        }
    }

    /**
     * Show personalized Dialog
     */
    private fun showDialog(requestKey: String, title: String, message: String) {
        ConfirmDialog.newInstance(
            requestKey = requestKey,
            title = title,
            message = message,
            positiveButtonText = "Aceptar",
            negativeButtonText = "Cancelar"
        ).show(supportFragmentManager, "ConfirmDialogTag")
    }

    /**
     * Change some things from the UI when the activity is launched to break a varita
     */
    private fun breakVaritaActions() {
        binding.tvCreateBreakeVarita.setText("Romper Varita")
        binding.btCreateBreak.setText("Romper Varita")
        binding.btCreateBreak.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        // Deshabilitamos campos porque es solo para romper
        binding.etWood.isEnabled = false
        binding.etCore.isEnabled = false
        binding.etLength.isEnabled = false

        binding.etWood.setText(intent.getStringExtra("wood"))
        binding.etCore.setText(intent.getStringExtra("core"))
        binding.etLength.setText(intent.getFloatExtra("length", 0.0f).toString())
    }

    private fun breakVarita(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.breakVarita(id)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        showToast("La varita se ha roto.")
                    } else {
                        showToast("Error al romper: ${response.code()}")
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) { showToast("Error: ${ex.message}") }
            }
        }
    }

    /**
     * Do a POST with the new varita to the API and notify the user if all goes well or not.
     */
    private fun createVarita() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val varita = CreateVaritaDTO(
                    binding.etWood.text.toString().trim(),
                    binding.etCore.text.toString().trim(),
                    binding.etLength.text.toString().toFloat()
                )
                val response = service.createVarita(varita)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        showToast("La varita se ha creado.")
                    } else {
                        showToast("Error al crear: ${response.code()}")
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) { showToast("Error: ${ex.message}") }
            }
        }
    }

    private fun checkData(): Boolean {
        if (intent.getIntExtra("id", -1) > -1) return true

        val wood = binding.etWood.text.toString().trim().length >= CreateVaritaDTO.MIN_LENGTH_WOOD
        val core = binding.etCore.text.toString().trim().length >= CreateVaritaDTO.MIN_LENGTH_CORE
        val lengthStr = binding.etLength.text.toString()
        val length = if (lengthStr.isNotEmpty()) lengthStr.toFloat() >= CreateVaritaDTO.MIN_LENGTH else false

        return wood && core && length
    }

    private fun showToast(text: String?) {
        if (!text.isNullOrBlank()) {
            binding.tvCustomToastText.text = text
            binding.cvCustomToast.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.cvCustomToast.visibility = View.GONE
            }, 3000)
        }
    }

    private fun setUpRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlApiService.URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}