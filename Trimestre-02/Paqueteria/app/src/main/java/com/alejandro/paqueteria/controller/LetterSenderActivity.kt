package com.alejandro.paqueteria.controller

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityLetterSenderBinding
import com.alejandro.paqueteria.dialogs.ConfirmationDialog
import com.alejandro.paqueteria.dialogs.WarningDialog
import com.alejandro.paqueteria.enums.Size
import com.alejandro.paqueteria.repository.MailRepository

class LetterSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterSenderBinding
    val CHANNEL_ID = "main_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLetterSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        val REQUEST_KEY_CODE = "ConfirmationCode"

        val spinner: Spinner = binding.spLetterSizes
        val sizes = enumValues<Size>()

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            sizes
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDimension = sizes[position]

                // 5. Actualizar los TextViews con las propiedades del Enum
                binding.tvCustomToastText.setText("Ancho: ${selectedDimension.width}cm, Alto: ${selectedDimension.height}cm.")
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                //Do Nothing. The CardView is showed for a specific period of time.
            }
        }

        /**
         * Throws the notification if the user accept to send the package
         */
        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODE, this) { requestKey,
                                                                                   bundle ->
            when (bundle.getString(ConfirmationDialog.RESULT_KEY_ACTION)) {
                ConfirmationDialog.ACTION_POSITIVE -> {
                    sendNotification()
                    MailRepository.incrementNumberOfLetters()
                }
                else -> {
                    //Do Nothing if cancel
                }
            }
        }

        binding.btCancelLetter.setOnClickListener {
            finish()
        }

        binding.btSendLetter.setOnClickListener {
            if(checkData()) {
                ConfirmationDialog.newInstance(REQUEST_KEY_CODE, "Enviar Carta Certificada",
                    "¿Estás seguro de que quieres enviar la carta?",
                    "Si", "No").show(supportFragmentManager, "Confirmation")
            } else {
                binding.tvCustomToastText.setText("La información no es correcta")
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }
    }

    /**
     * Check if the fields contains the required data and if not, shows a dialog that awares the user
     */
    private fun checkData(): Boolean {
        val isSenderCorrect = !binding.etSenderLetter.text.isNullOrBlank()
        val isDestinationCorrect = !binding.etDestinationLetter.text.isNullOrBlank()
        val errors = mutableListOf<WarningDialog>()
        if(!isSenderCorrect) errors.add(WarningDialog.newInstance(
            "Remitente vacío", "El remitente no puede estar vacío"
        ))
        if(!isDestinationCorrect) errors.add(WarningDialog.newInstance(
            "Destinatario vacío", "El destinatario no puede estar vacío"
        ))
        errors.forEach { it.show(supportFragmentManager, "WarningDialogTag") }
        return isSenderCorrect && isDestinationCorrect
    }

    //NOTIFICATIONS FUNCTIONS

    /**
     * Check if the app has permissions to launch notifications
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted) {
                binding.tvCustomToastText.setText("No se tienen permisos para lanzar notificaciones. Concedelos.")
                binding.cvCustomToast.visibility = View.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.cvCustomToast.visibility = View.GONE
                }, 3000)
            }
        }

    /**
     * Ask the user to give the apps the permission to launch notifications
     */
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

    /**
     * Create the notification channel to launch notifications
     */
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

    /**
     * Send the notification if has permissions, else ask the suer to give it.
     */
    private fun sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }

        val intent = Intent(this, ReceptionActivity::class.java)

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Carta certificada recibida")
            .setContentText("Has recibido una carta certificada de ${binding.etSenderLetter.text}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }
}