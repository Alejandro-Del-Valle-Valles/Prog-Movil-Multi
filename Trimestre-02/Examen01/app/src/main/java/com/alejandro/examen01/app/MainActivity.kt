package com.alejandro.examen01.app

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alejandro.examen01.R
import com.alejandro.examen01.databinding.ActivityMainBinding
import com.alejandro.examen01.helper.ConfirmDialog
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "canal_noti"
    val REQUEST_KEY_CODIGO = "Confirmar"
    private val MIME_TYPE_TEXT = "text/plain"
    private val crearLanzadorFicheros = registerForActivityResult(
        ActivityResultContracts.CreateDocument(MIME_TYPE_TEXT)) { uri ->
        if(uri != null)
            crearFichero(uri)
        else
            Toast.makeText(this, "Se canceló el guardado", Toast.LENGTH_SHORT)
                .show()

    }

    private val leerLanzadorFichero = registerForActivityResult(ActivityResultContracts.GetContent()) {
        uri ->
        if(uri != null) leerContenidoFichero(uri)
        else Toast.makeText(this, "Se canceló la selección", Toast.LENGTH_SHORT)
            .show()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted)
                Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)
        createNotificationChannel()

        supportFragmentManager.setFragmentResultListener(
            REQUEST_KEY_CODIGO,
            this
        ) { requestKey, bundle ->
            when (bundle.getString(ConfirmDialog.RESULT_KEY_ACTION)) {
                ConfirmDialog.ACTION_POSITIVE -> {
                    val player = MediaPlayer.create(this, R.raw.audio_examen)
                    player.start()
                    player.setOnCompletionListener { player.release() }
                }
            }
        }

        binding.btAbrirFichero.setOnClickListener {
            leerLanzadorFichero.launch("text/*")
        }

        binding.btGuardarFichero.setOnClickListener {
            if (!binding.etTexto.text.isEmpty()) crearLanzadorFicheros.launch("text/*")
        }
    }

    private fun crearFichero(uri: Uri) {
        val contenido = binding.etTexto.text.toString().trim()
        try {
            contentResolver.openOutputStream(uri)?.use { salida ->
                salida.write(contenido.toByteArray())
                Toast.makeText(this, "Arhcivo creado en $uri", Toast.LENGTH_LONG)
                    .show()
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Error al crear el fichero.", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun leerContenidoFichero(uri: Uri) {
        try {
            contentResolver.openInputStream(uri)?.use { lectura ->
                val lector = BufferedReader(InputStreamReader(lectura))
                val contenido: String? = lector.readText()

                if (contenido != null) {
                    if (contenido.length < 50)
                        Toast.makeText(this, contenido, Toast.LENGTH_LONG)
                            .show()
                    else if (contenido.length <= 100)
                        ConfirmDialog.newInstance(
                            REQUEST_KEY_CODIGO,
                            "Contenido del fichero",
                            contenido,
                            "Aceptar"
                        ).show(supportFragmentManager, "EviarDialog")
                    else
                        sendNotification(contenido)
                } else Toast.makeText(this, "Contenido vacío", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "Error al leer el fichero", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
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

    private fun sendNotification(contenido: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }

        val intent = Intent(this, CuadradoActivity::class.java)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle("Contenido del fichero")
            .setContentText(contenido)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }


}