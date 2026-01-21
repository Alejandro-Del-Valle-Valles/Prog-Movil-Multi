package com.alejandro.practicatipoexamen.activity

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
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import com.alejandro.practicatipoexamen.R
import com.alejandro.practicatipoexamen.databinding.ActivityMainBinding
import com.alejandro.practicatipoexamen.helper.ConfirmDialog
import androidx.core.view.isNotEmpty

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val CHANNEL_ID = "mi_canal"
    var CONTADOR_CARTAS = 1

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
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.cvCustomToast.visibility = View.GONE
                    }, 3000) // 3000
                }
            }
        }

        binding.btEnviar.setOnClickListener {
            if(comprobarDatosCartas() && binding.llContenedorCartas.isNotEmpty()) {
                ConfirmDialog.newInstance(
                    REQUEST_KEY_CODIGO,
                    "¿Quieres enviar las cartas?",
                    "¿Quieres enviar las cartas?",
                    "Enviar",
                    "Cancelar Envio"
                ).show(supportFragmentManager, "EnviarDialog")
            } else {
                binding.tvCustomToastText.text = if(binding.llContenedorCartas.isEmpty()) "Tienes que agregar cartas"
                else "Hay campos vacíos"
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }

        binding.btAdd.setOnClickListener {
            agregarVista()
        }

        binding.btRemove.setOnClickListener {
            if(binding.llContenedorCartas.isNotEmpty()) {
                binding.llContenedorCartas.removeViewAt(binding.llContenedorCartas.childCount -1)
                binding.llContenedorCartas.removeViewAt(binding.llContenedorCartas.childCount -1)
                binding.llContenedorCartas.removeViewAt(binding.llContenedorCartas.childCount -1)
                binding.llContenedorCartas.removeViewAt(binding.llContenedorCartas.childCount -1)
                CONTADOR_CARTAS--
            }
        }
    }

    private fun agregarVista() {
        val tvNumCarta = TextView(this)
        tvNumCarta.text = "Carta $CONTADOR_CARTAS"
        CONTADOR_CARTAS++

        val etRemitente = EditText(this)
        etRemitente.hint = "Remitente..."
        etRemitente.textSize = 16f
        etRemitente.id = View.generateViewId()

        val etContenido = EditText(this)
        etContenido.hint = "Contenido..."
        etContenido.textSize = 16f
        etContenido.id = View.generateViewId()

        val etDestinatario = EditText(this)
        etDestinatario.hint = "Destinatario..."
        etDestinatario.textSize = 16f
        etDestinatario.id = View.generateViewId()

        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        params.setMargins(0, 0, 0, 16)

        tvNumCarta.layoutParams = params
        etDestinatario.layoutParams = params
        etContenido.layoutParams = params
        etRemitente.layoutParams = params
        binding.llContenedorCartas.addView(tvNumCarta)
        binding.llContenedorCartas.addView(etDestinatario)
        binding.llContenedorCartas.addView(etContenido)
        binding.llContenedorCartas.addView(etRemitente)

        binding.svCartas.post {
            binding.svCartas.fullScroll(LinearLayout.FOCUS_DOWN)
        }
    }

    //TODO: Añadir lógica de almacenamiento de cartas en una BBDD
    private fun enviarCartas() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }

        val intent = Intent(this, CartasActivity::class.java)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)

            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black_24dp) // ¡Obligatorio! Un icono pequeño
            .setContentTitle("Cartas Enviadas")
            .setContentText("Has recibido nuevas cartas")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }

    private fun comprobarDatosCartas(): Boolean {
        for(i in 0 until binding.llContenedorCartas.childCount) {
            val hijo = binding.llContenedorCartas.getChildAt(i)
            if(hijo is EditText && hijo.text.isEmpty()) return false //Cuando falta contenido automaticamente sale
        }
        return true
    }


    //PERMISOS NOTIFICACIONES
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                binding.tvCustomToastText.text = "Permisos de Notificaciones no otorgado"
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }
    private fun askForNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                enviarCartas()
            } else {
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

            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}