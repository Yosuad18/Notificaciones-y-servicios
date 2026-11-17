package com.example.serviciosnotificaciones

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private val CHANNEL_ID = "canal_notificaciones_ej5"

    // Lanzador para solicitar permiso de notificaciones en Android 13+
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Crear canal de notificación para Android 8.0+
        crearCanalNotificacion()

        // Solicitar permiso de notificaciones en Android 13+
        verificarPermisoNotificacion()

        // Botón Ejercicio 1
        findViewById<Button>(R.id.btnIniciarContador).setOnClickListener {
            startService(Intent(this, ServicioContador::class.java))
        }

        // Botón Ejercicio 2
        findViewById<Button>(R.id.btnIniciarOperacion).setOnClickListener {
            val intent = Intent(this, ServicioOperacion::class.java).apply {
                putExtra("numero", 5)
            }
            startService(intent)
        }

        // Botón Ejercicio 3
        findViewById<Button>(R.id.btnIniciarDescarga).setOnClickListener {
            startService(Intent(this, ServicioDescarga::class.java))
        }

        // Botón Ejercicio 4
        val etNumero = findViewById<EditText>(R.id.etNumero)
        findViewById<Button>(R.id.btnCalcularCuadrado).setOnClickListener {
            val texto = etNumero.text.toString()
            if (texto.isNotEmpty()) {
                try {
                    val valor = texto.toDouble()
                    val intent = Intent(this, ServicioOperacion::class.java).apply {
                        putExtra("numero", valor)
                    }
                    startService(intent)
                    Toast.makeText(this, "Número $valor enviado", Toast.LENGTH_SHORT).show()
                } catch (e: NumberFormatException) {
                    Toast.makeText(this, "Ingrese un número válido", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Campo vacío", Toast.LENGTH_SHORT).show()
            }
        }

        // Botón Ejercicio 5:
        findViewById<Button>(R.id.btnCrearNotificacion).setOnClickListener {
            lanzarNotificacionConBotones()
        }
    }
    private fun crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canal Ejercicio 5"
            val descriptionText = "Canal para notificaciones con acciones"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun verificarPermisoNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
    private fun lanzarNotificacionConBotones() {
        // 1. Intent y PendingIntent para "Ver detalles"
        val intentDetalle = Intent(this, DetalleActivity::class.java)
        val pendingIntentDetalle = PendingIntent.getActivity(
            this,
            0,
            intentDetalle,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 2. Intent y PendingIntent para "Cancelar"
        val intentCancelar = Intent(this, NotificacionReceiver::class.java).apply {
            action = "ACCION_CANCELAR"
        }
        val pendingIntentCancelar = PendingIntent.getBroadcast(
            this,
            1,
            intentCancelar,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // 3. Construcción de la notificación
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setContentTitle("Ejercicio 5: Notificación Interactiva")
            .setContentText("Seleccione una de las acciones disponibles:")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            //  Ver detalles
            .addAction(android.R.drawable.ic_menu_view, "Ver detalles", pendingIntentDetalle)
            // Cancelar
            .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Cancelar", pendingIntentCancelar)

        // 4. Mostrar la notificación
        with(NotificationManagerCompat.from(this)) {
            // Verificar permiso antes de notificar en Android 13+
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED || Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU
            ) {
                notify(1, builder.build())
            }
        }
    }
}