package com.example.serviciosnotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ServicioDescarga : Service() {
    companion object {
        private const val TAG = "ServicioDescarga"
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Servicio de descarga iniciado (onStartCommand)")

        //simular la descarga de 10 archivos
        Thread {
            try {
                for (i in 1..10) {
                    Thread.sleep(1000)
                    Log.d(TAG, "Archivo descargado: $i de 10")
                }
                //mostrar el mensaje final
                Log.d(TAG, "Descarga completada")
            } catch (e: InterruptedException) {
                Log.d(TAG, "Descarga interrumpida")
            } finally {
                // Detener el servicio
                stopSelf(startId)
            }
        }.start()
        return START_NOT_STICKY
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Servicio de descarga destruido (onDestroy)")
    }
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}