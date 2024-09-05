package com.example.nadjistan.data.services

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nadjistan.MainActivity
import com.example.nadjistan.ProvideLocation
import com.example.nadjistan.R
import com.example.nadjistan.data.objects.ContextProvider
import com.example.nadjistan.data.repositories.PonudeRepository
import com.example.nadjistan.data.objects.RepositoryProvider
import com.example.nadjistan.data.objects.ServerMessage
import com.example.nadjistan.data.tools.DefaultLocationClient
import com.example.nadjistan.data.tools.LocationClient
import com.example.nadjistan.data.tools.LocationProvider
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DataRefreshService() : Service() {
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var runnable2: Runnable
    private lateinit var repository: PonudeRepository
    private lateinit var locationProvider: LocationProvider
    private lateinit var context: Context

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        repository = RepositoryProvider.GetRepository()
        locationProvider = ProvideLocation()
        context = ContextProvider.get()

        handler = Handler(Looper.getMainLooper())

        runnable = object : Runnable {
            val r = this
            override fun run() {
                serviceScope.launch {
                    // Pozovi funkciju koju želiš periodično izvršavati
                    repository.GetPonude()
                    Log.d("DATA REFRESH SERVICE", "Updating location")
                    repository.updateLocationData(
                        locationProvider.getLat(),
                        locationProvider.getLong()
                    )

                    // Ponovo zakazuj ovu funkciju
                    handler.postDelayed(r, 10000) // 10 sekundi
                }
            }
        }

       /* runnable2 = object : Runnable {
            override fun run() {

                ServerMessage.Near(locationProvider.getLat(), locationProvider.getLong()){

                        val intent = Intent(context, MainActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent,
                            PendingIntent.FLAG_IMMUTABLE)

                        Log.d("NOTIFY_WORKER","Making notification")
                        val notification = NotificationCompat.Builder(context, "locationservicechannel")
                            .setContentTitle("Ponude u blizini")
                            .setContentText("Kliknite za pregled lokalnih ponuda")
                            .setSmallIcon(R.drawable.img)
                            .setContentIntent(pendingIntent)
                            .setOngoing(false)

                        val notificationManager =
                            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                        Log.d("NOTIFY_WORKER","Notifying...")
                        notificationManager.notify(1, notification.build())

                }

                handler.postDelayed(this, 300000)
            }
        }*/

    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        start()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        serviceScope.launch {
            // Pokreni periodičan zadatak
            handler.post(runnable)
            // handler.post(runnable2)
            Log.d("DATA REFRESH SERVICE", "Service started.")
        }
    }

    private fun stop() {
        Log.d("DATA REFRESH SERVICE", "Service stopped.")
        handler.removeCallbacks(runnable)
        //handler.removeCallbacks(runnable2)
        stopSelf()
    }

    override fun stopService(name: Intent?): Boolean {
        stop()
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }


}