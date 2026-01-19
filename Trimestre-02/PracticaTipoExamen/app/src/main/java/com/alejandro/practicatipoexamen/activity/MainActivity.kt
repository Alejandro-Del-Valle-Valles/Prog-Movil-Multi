package com.alejandro.practicatipoexamen.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.alejandro.practicatipoexamen.databinding.ActivityMainBinding
import com.alejandro.practicatipoexamen.helper.ConfirmDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "mi_canal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel() //Creamos el canal de notificaciones

        val REQUEST_KEY_CODIGO = "ConfirmarEnvio"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODIGO, this) {
            requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    askForNotificationPermission()
                }
                ConfirmDialog.ACTION_NEGATIVE -> {
                    binding.tvCustomToastText.text = "Envio cancelado"
                    binding.cvCustomToast.visibility = View.VISIBLE
                    // Usamos el propio Handler del hilo principal
                    Handler(Looper.getMainLooper()).postDelayed({
                        // Este código se ejecutará después del retraso
                        binding.cvCustomToast.visibility = View.GONE
                    }, 3000) // 3000
                }
            }
        }

        binding.btEnviar.setOnClickListener {
            //Crea la instancia del dialog con la pregunta
            ConfirmDialog.newInstance(
                REQUEST_KEY_CODIGO,
                "¿Quieres enviar las cartas?",
                "Confirmar Envio",
                "Cancelar Envío"
            ).show(supportFragmentManager, "EnviarDialog")
        }
    }

    private fun enviarCartas() {
        //TODO: Lógica de reproducir sonido y enviar cartas.
    }

    //PERMISOS NOTIFICACIONES

    // 1. Prepara el "lanzador" para pedir el permiso
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso CONCEDIDO. Ya puedes enviar notificaciones.
                // (Opcional: puedes llamar aquí a tu función de enviar notificación si quieres)
            } else {
                // Permiso DENEGADO.
                binding.tvCustomToastText.text = "Permisos de Notificaciones no otorgado"
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }

    // 2. Función de ayuda para comprobar y pedir el permiso
    private fun askForNotificationPermission() {
        // Solo es necesario en Android 13 (API 33) y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Comprueba si el permiso YA está concedido
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                enviarCartas()
            } else {
                // No tienes permiso, pídelo
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    //NOTIFICACIONES
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Principal"
            val descriptionText = "Notificaciones generales de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            // 1. Define el canal
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // 2. Registra el canal en el sistema
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}