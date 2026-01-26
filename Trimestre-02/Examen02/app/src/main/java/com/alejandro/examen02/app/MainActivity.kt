package com.alejandro.examen02.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alejandro.examen02.R
import com.alejandro.examen02.data.Vuelo
import com.alejandro.examen02.databinding.ActivityMainBinding
import com.alejandro.examen02.enums.TipoVuelo
import com.alejandro.examen02.helper.ConfirmDialog
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "mi_canal_principal"
    private val vistaModelo: VistaModeloVuelo by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        val REQUEST_KEY_CODIGO = "ElegirVuelo"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODIGO, this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    lanzarRegistro(TipoVuelo.NACIONAL)
                }
                ConfirmDialog.ACTION_NEGATIVE -> {
                    lanzarRegistro(TipoVuelo.EUROPEO)
                }
                ConfirmDialog.ACTION_NEUTRAL -> {
                    lanzarRegistro(TipoVuelo.LARGA_DISTANCIA)
                }
            }
        }


        binding.btRegistrar.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_CODIGO,
                "Elije el tipo de vuelo a registrar",
                "Selecciona uno de los 3 tipos de vuelos",
                "Nacional",
                "Europeo",
                "Larga Distancia"
            ).show(supportFragmentManager, "EnviarDialog")
        }

        binding.btNumVuelos.setOnClickListener {
            val numero = vistaModelo.getAll().value?.size ?: 0
            Toast.makeText(this, "Hay un total de $numero vuelos.", Toast.LENGTH_SHORT)
                .show()
        }

        binding.btRegrsitros.setOnClickListener {
            sendNotification()
        }
    }

    private fun lanzarRegistro(tipoVuelo: TipoVuelo) {
        intent = Intent(this, RegistroActivity::class.java)
        intent.putExtra("tipo", tipoVuelo.name)
        startActivity(intent)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Principal"
            val descriptionText = "Notificaciones generales de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }

        val intent = Intent(this, ConsultaActivity::class.java)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("¡Mira los vuelos!")
            .setContentText("Pulsa la noti para ver todos los vuelos")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                sendNotification()
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
            }
        }
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                sendNotification()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}