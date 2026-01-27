package com.alejandro.simulacroexamen01.app

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
import androidx.lifecycle.LiveData
import com.alejandro.simulacroexamen01.R
import com.alejandro.simulacroexamen01.TipoTropas
import com.alejandro.simulacroexamen01.data.Tropa
import com.alejandro.simulacroexamen01.databinding.ActivityMainBinding
import com.alejandro.simulacroexamen01.helper.ConfirmDialog
import kotlin.getValue

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "mi_canal_principal"
    private val vistaModelo: VistaModeloTropa by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()

        vistaModelo.todasTropas.observe(this) { tropas ->
            //No hacemos nada
        }

        val REQUEST_KEY_CODIGO = "ElegirTropa"

        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODIGO
            , this) {
                requestKey, bundle ->
            when(bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", TipoTropas.ZERG.name)
                    startActivity(intent)
                }
                ConfirmDialog.ACTION_NEGATIVE -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", TipoTropas.TERRAN.name)
                    startActivity(intent)
                }
                ConfirmDialog.ACTION_NEUTRAL -> {
                    intent = Intent(this, RegistroActivity::class.java)
                    intent.putExtra("tipo", TipoTropas.PROTOSS.name)
                    startActivity(intent)
                }
            }
        }


        binding.btReclutar.setOnClickListener {
            ConfirmDialog.newInstance(
                REQUEST_KEY_CODIGO,
                "Elige el tipo de tropa a reclutar",
                "Elige uno de los 3 tipos de tropas a recultar",
                TipoTropas.ZERG.name,
                TipoTropas.TERRAN.name,
                TipoTropas.PROTOSS.name
            ).show(supportFragmentManager, "EnviarDialog")
        }

        binding.btInforme.setOnClickListener {
            Toast.makeText(this, "Tienes ${vistaModelo.todasTropas.value?.size ?: 0} tropas", Toast.LENGTH_LONG)
                .show()
        }

        binding.btInvasion.setOnClickListener {
            if(vistaModelo.todasTropas.value != null) sendNotification()
            else Toast.makeText(this, "Aún no hay tropas", Toast.LENGTH_LONG)
                .show()
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
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.POST_NOTIFICATIONS) !=
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
            .setContentTitle("¡Alerta!")
            .setContentText("Unidad enemiga detectada")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }

}