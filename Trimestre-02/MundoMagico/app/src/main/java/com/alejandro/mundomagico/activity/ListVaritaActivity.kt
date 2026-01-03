package com.alejandro.mundomagico.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alejandro.mundomagico.databinding.ActivityListVaritaBinding
import com.alejandro.mundomagico.model.VaritaDTO
import com.alejandro.mundomagico.service.ApiService
import com.alejandro.mundomagico.service.UrlApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListVaritaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListVaritaBinding
    private lateinit var service: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListVaritaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpRetrofit()

        getVaritasFromApi()

        binding.btExitManagment.setOnClickListener {
            finish()
        }
    }

    /**
     * Get all varitas from the API and show in the list if all is correct.
     * Else notify the user with the error.
     */
    private fun getVaritasFromApi() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = service.getAllVaritas()

                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        val varitas = response.body()
                        if(varitas != null) showVaritas(varitas)
                        else showToast("Ha ocurrido un error al obtener las varitas.")
                    } else {
                        showToast("Ha ocurrido un error al obtener las varitas.")
                    }
                }
            } catch (ex: Exception) {
                showToast(ex.message)
            }
        }
    }

    /**
     * Show the varitas in the list
     */
    private fun showVaritas(varitas: List<VaritaDTO>) {
        val displayList = varitas.map { "Madera: ${it.madera}, Núcleo: ${it.nucleo}, Personaje: ${it.personaje}" }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            displayList
        )

        binding.lvVaritas.adapter = adapter

        binding.lvVaritas.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedVarita = varitas[position]
            val newIntent = Intent(this, VaritaActivity::class.java)
            newIntent.putExtra("id", selectedVarita.id)
            newIntent.putExtra("wood", selectedVarita.madera)
            newIntent.putExtra("core", selectedVarita.nucleo)
            newIntent.putExtra("length", selectedVarita.longitud)
            startActivity(newIntent)
        }
    }

    /**
     * Show for 3 seconds a custom toast with custom message
     */
    private fun showToast(text: String?) {
        if(!text.isNullOrBlank()) {
            binding.tvErrorLoadingText.setText("Ha ocurrido un error: " + text)
            binding.cvErrorLoading.visibility = View.VISIBLE

            Handler(Looper.getMainLooper()).postDelayed({
                binding.cvErrorLoading.visibility = View.GONE
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