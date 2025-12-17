package com.alejandro.ejercicio_apis.controller

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alejandro.ejercicio_apis.databinding.ActivityMainBinding
import com.alejandro.ejercicio_apis.model.PostBlog
import com.alejandro.ejercicio_apis.service.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var service: ApiService
    private var postCounter = 1
    private val listOfPost = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupRetrofit()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOfPost)
        binding.lvPosts.adapter = adapter

        binding.btClean.setOnClickListener {
            binding.etTitle.text.clear()
            binding.etBody.text.clear()
        }

        binding.btSend.setOnClickListener {
            if(checkData()) sendPost()
            else {
                binding.tvCustomToastText.setText("Campos vacíos")
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }
    }

    private fun setupRetrofit() {
        val retofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retofit.create(ApiService::class.java)
    }

    private fun sendPost() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val newPost = PostBlog(
                    userId = postCounter,
                    title = binding.etTitle.text.toString(),
                    body = binding.etBody.text.toString()
                )
                postCounter++

                val response = service.postPost(newPost)
                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        val createdPost = response.body()
                        listOfPost.add(createdPost?.toString() ?: "ID: ${postCounter - 1} - Desconocido")
                        adapter.notifyDataSetChanged()
                    } else {
                        binding.tvCustomToastText.setText("No se ha creado el post")
                        binding.cvCustomToast.visibility = View.VISIBLE
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.cvCustomToast.visibility = View.GONE
                        }, 3000)
                    }
                }
            } catch (ex: Exception) {
                binding.tvCustomToastText.setText(ex.message)
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }
    }

    private fun checkData(): Boolean = !binding.etTitle.text.isNullOrBlank()
            && !binding.etBody.text.isNullOrBlank()
}