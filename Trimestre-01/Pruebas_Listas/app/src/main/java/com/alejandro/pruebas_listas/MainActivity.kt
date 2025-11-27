package com.alejandro.pruebas_listas

import android.os.Bundle
import android.util.ArraySet
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.pruebas_listas.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var messages: ArrayList<String>
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messages = ArrayList<String>()
        adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1,
            messages)
        binding.lvMessages.adapter = adapter
    }

    /**
     * When send button is clicked, this checks that the text of the editText isn't
     * empty or is already on the list of messages. If is empty or is in the list,
     * notify the user
     */
    fun onSendClicked(view: View) {
        var newMessage = binding.etMessage.text.toString().trim()
        binding.etMessage.setText("")
        if(newMessage.isNotBlank() && !messages.contains(newMessage)) {
            messages.add(newMessage)
            adapter.notifyDataSetChanged()
        } else {
            Toast.makeText(this,
                "No se pueden introducir mensajes en blanco o repetidos.",
                Toast.LENGTH_SHORT).show()
        }
    }

}