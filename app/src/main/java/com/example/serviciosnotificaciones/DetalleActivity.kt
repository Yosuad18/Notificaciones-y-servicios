package com.example.serviciosnotificaciones

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetalleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalle)

        val tvMensaje = findViewById<TextView>(R.id.tvMensajeDetalle)
        tvMensaje.text = "¡Bienvenido a DetalleActivity!\n" +
                "Abierto desde la notificación del Ejercicio 5."
    }
}