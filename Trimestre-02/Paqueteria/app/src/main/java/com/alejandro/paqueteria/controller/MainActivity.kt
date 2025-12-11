package com.alejandro.paqueteria.controller

import android.content.Intent
import com.alejandro.paqueteria.R
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alejandro.paqueteria.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.SendPackage -> {
                intent = Intent(this, PackageSenderActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.SendMilCertificated -> {
                intent = Intent(this, LetterSenderActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.MailReception -> {
                intent = Intent(this, ReceptionActivity::class.java)
                intent.putExtra("IsFromMenu", true)
                startActivity(intent)
                return true
            }
            else -> return false
        }
    }
}