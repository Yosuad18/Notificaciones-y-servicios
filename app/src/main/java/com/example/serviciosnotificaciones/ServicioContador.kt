package com.example.serviciosnotificaciones

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class ServicioContador : Service() {

    private var hiloContador: Thread? = null
    private var isRunning = false

    companion object {
        private const val TAG = "ServicioContador"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Servicio creado (onCreate)")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Servicio iniciado (onStartCommand)")
        if (!isRunning) {
            isRunning = true

            hiloContador = Thread {
                try {
                    for (i in 1..10) {
                        if (!isRunning) break
                        Log.d(TAG, "Valor: $i")
                        Thread.sleep(1000) // Pausa de 1 segundo
                    }
                } catch (e: InterruptedException) {
                    Log.d(TAG, "Hilo interrumpido")
                } finally {

                    stopSelf(startId)
                }
            }
            hiloContador?.start()
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "Servicio destruido (onDestroy)")
        isRunning = false
        hiloContador?.interrupt()
        hiloContador = null
    }

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }
}