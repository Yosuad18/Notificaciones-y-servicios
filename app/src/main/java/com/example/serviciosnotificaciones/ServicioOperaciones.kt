package com.example.serviciosnotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ServicioOperacion : Service() {

    companion object  {
        private const val TAG = "ServicioOperacion"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Servicio iniciado (onStartCommand)")

        //si se envió un Double (Ejercicio 4) o un Int (Ejercicio 2)
        if (intent?.hasExtra("numero") == true) {
            //recuperar Double (Ejercicio 4)
            val numeroDouble = intent.getDoubleExtra("numero", -1.0)

            if (numeroDouble != -1.0) {
                Thread {
                    val cuadrado = numeroDouble * numeroDouble
                    Log.d(TAG, "El cuadrado de $numeroDouble es: $cuadrado")
                    stopSelf(startId)
                }.start()
            } else {
                // Si no es Double,Ejercicio 2
                val numeroInt = intent.getIntExtra("numero", 0)
                Thread {
                    val factorial = calcularFactorial(numeroInt)
                    Log.d(TAG, "El factorial de $numeroInt es: $factorial")
                    stopSelf(startId)
                }.start()
            }
        }
        return START_NOT_STICKY
    }
    private fun calcularFactorial(n: Int): Long {
        var resultado: Long = 1
        for (i in 1..n) {
            resultado *= i
        }
        return resultado
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Servicio destruido (onDestroy)")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}