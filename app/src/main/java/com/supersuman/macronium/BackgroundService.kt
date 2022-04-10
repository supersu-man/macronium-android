package com.supersuman.macronium

import android.app.*
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class BackgroundService : Service() {

    private val socky = Socky(this)

    @Deprecated("Deprecated in Java")
    override fun onStart(intent: Intent?, startId: Int) {
        when(intent?.action) {
            "CONNECT" -> {
                startForeground(69, showNotification())
                val qrstring = intent.getStringExtra("qrstring").toString()
                socky.connectSocket(qrstring)
            }
            "SEND_MESSAGE" -> {
                val key = intent.getStringExtra("key").toString()
                val arg = intent.getStringExtra("arg").toString()
                if (key == "key-press"){
                    socky.sendMessage("key-press", arg)
                }
            }
        }
    }

    override fun onDestroy() {
        try {
            socky.disconnectSocket()
            stopForeground(true)
            stopSelf()
        }catch (e:Exception){}
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showNotification(): Notification {

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(this, 0, intent, FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(this, 0, intent, 0)
        }

        val builder = NotificationCompat.Builder(this, "69")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Connected")
            .setContentText("Macronium is keeping the connection alive")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Background service"
            val descriptionText = "Notifications related to background services"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("69", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return builder
    }
}