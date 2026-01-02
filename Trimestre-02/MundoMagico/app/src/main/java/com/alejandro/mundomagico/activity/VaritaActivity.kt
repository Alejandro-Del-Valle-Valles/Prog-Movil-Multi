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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRetrofit()

        val idVarita = intent.getIntExtra("id", -1)
        val askMessage = "¿Estás seguro de que quires ${if(idVarita > -1) "crear" else "romper"} la varita?"
        if(idVarita > -1) breakVaritaActions()

        binding.btExitCreateBreakVarita.setOnClickListener {
            finish()
        }

        binding.btCreateBreak.setOnClickListener {
            if(checkData() && askUserConfirmation(askMessage)) {
                if(idVarita > -1) breakVarita(idVarita)
                else createVarita()
                clearFields()
            }
        }

        /*TODO: Comprobar la información introducida es correcta y rellenar la información de la varita
            cuando viene de ListVaritaActivity.
            Mostrar dialog cuando se va a crear/romper una varita confirmando que se quiere realzar dicha acción
            También cuando se va a salir de la Activity
            Mostrar CardView cuando hay algún error o cuando se crea/rompe una varita
            Método para crear la varita
         */
    }

    /**
     * When the activity is started to destroy a Varita, this change some texts and info
     * to show the correct and needed information
     */
    private fun breakVaritaActions() {
        binding.tvCreateBreakeVarita.setText("Romper Varita")
        binding.cbIsBorken.visibility = View.GONE
        binding.btCreateBreak.setText("Romper Varita")
        binding.btCreateBreak.backgroundTintList = ColorStateList.valueOf(Color.YELLOW)
        binding.etWood.isEnabled = false
        binding.etCore.isEnabled = false
        binding.etLength.isEnabled = false

        binding.etWood.setText(intent.getStringExtra("wood"))
        binding.etCore.setText(intent.getStringExtra("core"))
        binding.etLength.setText(intent.getFloatExtra("length", 0.0f).toString())
    }

    /**
     * Break the given varita and notify the user if it was broken or not
     */
    private fun breakVarita(id: Int) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.breakVarita(id)

                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        val varita = response.body()
                        if(varita != null) showToast("La varita se ha roto.")
                        else showToast("Ha ocurrido un error y no se ha podido romper la varita.")
                    } else {
                        showToast("Ha ocurrido un error al romper la varita")
                    }
                }
            } catch (ex: Exception) {
                showToast(ex.message)
            }
        }
    }


    private fun createVarita() {
        //TODO: Método que crea la nueva varita y hace petición POST a la API.
    }

    /**
     * Show for 3 seconds a custom toast with custom message
     */
    private fun showToast(text: String?) {
        if(!text.isNullOrBlank()) {
            binding.tvCustomToastText.setText(text)
            binding.tvCustomToastText.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.tvCustomToastText.visibility = View.GONE
            }, 3000)
        }
    }

    private fun checkData(): Boolean {
        var isCorrect = false
        //TODO: Comprobar que los campos están correctamente rellenados
        return isCorrect
    }

    private fun clearFields() {
        binding.etWood.setText("")
        binding.etCore.setText("")
        binding.etLength.setText("")
        binding.cbIsBorken.isChecked = false
    }

    private fun askUserConfirmation(question: String): Boolean {
        val confirmation = false
        //TODO: Lanzar un Dialog pididendo al usuario que confirme si quiere realizar la acción
        return confirmation
    }

    private fun setUpRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl(UrlApiService.URL_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(ApiService::class.java)
    }
}