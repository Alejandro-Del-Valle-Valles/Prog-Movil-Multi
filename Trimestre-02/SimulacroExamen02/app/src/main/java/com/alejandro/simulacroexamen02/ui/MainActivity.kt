package com.alejandro.simulacroexamen02.ui

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
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.alejandro.simulacroexamen02.R
import com.alejandro.simulacroexamen02.databinding.ActivityMainBinding
import com.alejandro.simulacroexamen02.helper.ConfirmDialog

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vistaModelo: VistaModeloHechizo by viewModels()
    val CHANNEL_ID = "mi_canal_principal"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        vistaModelo.todosHechizo.observe(this) {hechizo ->
            //No hago nada, pero necesito esto
        }

        val REQUEST_KEY_CODIGO = "InscribirHechizo"
        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODIGO
            , this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", "fuego")
                    startActivity(intent)
                }
                ConfirmDialog.ACTION_NEGATIVE -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", "hielo")
                    startActivity(intent)
                }
                ConfirmDialog.ACTION_NEUTRAL -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", "rayo")
                    startActivity(intent)
                }
            }
        }


        binding.btInscribir.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_CODIGO,
                "Elige un hechizo",
                "Selecciona el tipo de hechizo que quieres crear",
                "Fuego",
                "Hielo",
                "Rayo"
            ).show(supportFragmentManager, "EnviarDialog")
        }

        binding.btNumHechizos.setOnClickListener {
            val numHechizos = vistaModelo.todosHechizo.value?.size ?: 0
            Toast.makeText(this, "Hay un total de $numHechizos hechizos.",
                Toast.LENGTH_LONG).show()
        }

        binding.btConsultar.setOnClickListener {
            if((vistaModelo.todosHechizo.value?.size ?: 0) > 0)
                sendNotification()
            else Toast.makeText(this, "No hay hechizos aún",
                Toast.LENGTH_SHORT).show()
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                sendNotification()
            } else {
                Toast.makeText(this, "Permiso de notificaciones denegado",
                    Toast.LENGTH_SHORT).show()
            }
        }
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                sendNotification()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Principal"
            val descriptionText = "Notificaciones generales de la app"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                CHANNEL_ID, name,
                importance
            ).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as
                        NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }
        val intent = Intent(this, ConsultaActivity::class.java)

        val pendingIntent: PendingIntent? =
            TaskStackBuilder.create(this).run {
                addNextIntentWithParentStack(intent)
                getPendingIntent(
                    0,
                    PendingIntent.FLAG_UPDATE_CURRENT or
                            PendingIntent.FLAG_IMMUTABLE
                )
            }
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("¡Nuevo Mensaje!")
            .setContentText("Has recibido una nueva actualización.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }

    }

}