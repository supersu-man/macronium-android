package com.supersuman.macronium

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class BackgroundService : Service() {
    private val socky = Socky(this)
    override fun onStart(intent: Intent?, startId: Int) {
        if (intent?.action == "CONNECT"){
            socky.connectSocket(intent.getStringExtra("result")!!)
            if (socky.isConnected()){
                showNotification()
            }
        }
        else if (intent?.action == "SEND_MESSAGE"){
            socky.sendMessage(intent.getStringExtra("message")!!)
        } else{
            println("Dummy")
        }
    }

    override fun onDestroy() {
        try {
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(69)
            socky.disconnectSocket()
        }catch (e:Exception){

        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun showNotification(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "69")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Connected")
            .setContentText("Macronium is keeping the connection alive")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
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
        with(NotificationManagerCompat.from(this)) {
            notify(69, builder.build())
        }
    }
}