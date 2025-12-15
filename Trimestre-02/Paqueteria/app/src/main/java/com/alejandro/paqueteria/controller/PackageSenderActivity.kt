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
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.alejandro.paqueteria.R
import com.alejandro.paqueteria.databinding.ActivityPackageSenderBinding
import com.alejandro.paqueteria.dialogs.ConfirmationDialog
import com.alejandro.paqueteria.dialogs.WarningDialog
import com.alejandro.paqueteria.model.Package

class PackageSenderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPackageSenderBinding
    val CHANNEL_ID = "main_channel"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPackageSenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        val REQUEST_KEY_CODE = "ConfirmationCode"

        /**
         * Throws the notification if the user accept to send the package
         */
        supportFragmentManager.setFragmentResultListener(REQUEST_KEY_CODE, this) { requestKey,
            bundle ->
                when (bundle.getString(ConfirmationDialog.RESULT_KEY_ACTION)) {
                    ConfirmationDialog.ACTION_POSITIVE -> {
                        sendNotification()
                    }

                    else -> {
                        //Do Nothing if cancel
                    }
                    }
            }

        binding.tvPackageInformation.setText("El paquete debe tener un mínimo de ${Package.MIN_WIDTH} (Largo) por " +
                "${Package.MIN_HEIGHT} (Alto) por ${Package.MIN_LONG} (Profundo); y un máximo de ${Package.MAX_WIDTH} (Largo) por " +
                "${Package.MAX_HEIGHT} (Alto) por ${Package.MAX_LONG} (Profundo). Tampoco puede pesar más de ${Package.MAX_WEIGHT}Kg.")

        binding.btCancelPackage.setOnClickListener {
            finish()
        }

        binding.btSendPackage.setOnClickListener {
            if(checkData()) {
                ConfirmationDialog.newInstance(REQUEST_KEY_CODE, "Enviar Paquete",
                    "¿Estás seguro de que quieres enviar el paquete?",
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
     * Check if all data is correct. If not, shows a Dialog with the incorrect data.
     * @return Boolean, true if all data is correct, false otherwise
     */
    private fun checkData(): Boolean {
        val weightString = binding.etWeightPackage.text.toString()
        val weight = if(weightString.isBlank()) 0f else weightString.toFloat()
        val size = parseSize()

        val errors = mutableListOf<WarningDialog>()

        val isSenderCorrect = !binding.etSenderPackage.text.isNullOrBlank()
        val isDestinationCorrect = !binding.etDestinationPackage.text.isNullOrBlank()

        if (size.contains(null)) {
            errors.add(WarningDialog.newInstance(
                "Dimensiones vacías/inválidas",
                "Las dimensiones deben tener el formato largoXanchoXprofundo y ser números válidos."
            ))
        } else {
            val length = size[0]!!
            val height = size[1]!!
            val depth = size[2]!!

            when {
                length < Package.MIN_WIDTH || length > Package.MAX_WIDTH -> {
                    errors.add(WarningDialog.newInstance(
                        "Largo incorrecto",
                        "El largo (L) debe estar entre ${Package.MIN_WIDTH} y ${Package.MAX_WIDTH}."
                    ))
                }
                height < Package.MIN_HEIGHT || height > Package.MAX_HEIGHT -> {
                    errors.add(WarningDialog.newInstance(
                        "Alto incorrecto",
                        "El alto (A) debe estar entre ${Package.MIN_HEIGHT} y ${Package.MAX_HEIGHT}."
                    ))
                }
                depth < Package.MIN_LONG || depth > Package.MAX_LONG -> {
                    errors.add(WarningDialog.newInstance(
                        "Profundidad incorrecta",
                        "La profundidad (P) debe estar entre ${Package.MIN_LONG} y ${Package.MAX_LONG}."
                    ))
                }
            }
        }
        val isWeightCorrect = weight > 0 && weight <= Package.MAX_WEIGHT

        if(!isSenderCorrect) errors.add(WarningDialog.newInstance("Remitente vacío", "El remitente no puede estar vacío."))
        if(!isDestinationCorrect) errors.add(WarningDialog.newInstance("Destinatario vacío", "El destinatario no puede estar vacío."))

        if(!isWeightCorrect) errors.add(WarningDialog.newInstance(
            "Peso no válido",
            "El peso no puede ser inferior o igual a 0 ni superior a ${Package.MAX_WEIGHT}Kg"
        ))

        if (errors.isNotEmpty()) {
            errors.forEach {
                it.show(supportFragmentManager, "WarningDialogTag")
            }
            return false
        }

        //If there's no errors, it will pass
        return true
    }

    /**
     * Parse the data from the editText of the sizes to a Array of floats. If the size of the array is different of 3
     * or some value isn't a float, it doesn't parse the sizes and return a Array of nulls
     * @return Array<Float?> size 3
     */
    private fun parseSize(): Array<Float?> {
        val size = arrayOfNulls<Float?>(3)
        try {
            val sizes = binding.etPackageDimensions.text.toString()
                .replace("x", "X").trim().split("X")
            if(sizes.size == 3) {
                for(i in 0 .. sizes.size - 1) {
                    size[i] = sizes[i].toFloat()
                }
            } else throw Exception("No se han introducido las 3 dimensiones separadas por X.")
        } catch (ex: Exception) {
            WarningDialog.newInstance("Error en los datos de tamaño", ex.message!! )
        }
        return size
    }

    //NOTIFICATIONS FUNCTIONS

    /**
     * Check if the app has permissions to launch notifications
     */
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permiso CONCEDIDO. Ya puedes enviar notificaciones.
                // (Opcional: puedes llamar aquí a tu función de enviar notificación si quieres)
            } else {
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
     * Send all data of the package.
     */
    private fun sendNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            askForNotificationPermission()
            return
        }

        val intent = Intent(this, ReceptionActivity::class.java).apply {
            intent.putExtra("notification", true)
            intent.putExtra("sender", binding.etSenderPackage.text.toString().trim())
            intent.putExtra("destination",binding.etDestinationPackage.text.toString().trim())
            intent.putExtra("size",parseSize().filterNotNull().toTypedArray())
            intent.putExtra("weight",binding.etWeightPackage.text.toString().toFloat())
            intent.putExtra("insured",binding.cbInsured.isChecked)
        }

        val pendingIntent: PendingIntent? = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Paquete recibido")
            .setContentText("Has recibido un paquete de ${binding.etSenderPackage.text}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            val notificationId = 1
            notify(notificationId, builder.build())
        }
    }
}