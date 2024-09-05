package com.example.nadjistan.data.worker

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.nadjistan.data.tools.LocationProvider
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.nadjistan.MainActivity
import com.example.nadjistan.ProvideLocation
import com.example.nadjistan.R
import com.example.nadjistan.data.objects.ServerMessage

class NotifyWorker (val context: Context, workerParams: WorkerParameters)
    : Worker(context, workerParams) {
    override fun doWork() : Result {

        Log.d("NOTIFY_WORKER","Starting work")
        val lp = ProvideLocation()
        ServerMessage.Near(lp.getLat(), lp.getLong()) {

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

        return Result.success()
    }
}