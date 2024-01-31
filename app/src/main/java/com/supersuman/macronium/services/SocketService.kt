package com.supersuman.macronium.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
import android.os.Build
import android.os.IBinder
import android.view.View
import androidx.core.app.NotificationCompat
import com.google.android.material.card.MaterialCardView
import com.supersuman.macronium.MainActivity
import com.supersuman.macronium.R
import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI
import java.net.URISyntaxException

var socket: Socket? = null
lateinit var address: String
const val port = 6969

lateinit var connectButton: MaterialCardView
lateinit var disconnectButton: MaterialCardView

class SocketService: Service() {

    private val channelId = "SocketServiceChannel"

    override fun onCreate() {
        super.onCreate()
        try {
            socket = IO.socket(URI.create("http://$address:$port"))
            socket?.on(Socket.EVENT_CONNECT) {
                println("Connected")
                CoroutineScope(Dispatchers.Main).launch {
                    connectButton.visibility = View.GONE
                    disconnectButton.visibility = View.VISIBLE
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    startForeground(1, createNotification(), FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)
                } else {
                    startForeground(1, createNotification())
                }
            }
            socket?.on(Socket.EVENT_DISCONNECT) {
                CoroutineScope(Dispatchers.Main).launch {
                    connectButton.visibility = View.VISIBLE
                    disconnectButton.visibility = View.GONE
                }
            }
            socket?.connect()
        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.disconnect()
    }

    override fun onBind(intent: Intent?): IBinder? { return null }

    private fun createNotification(): Notification {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(channelId, "Background service",
                NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Connected")
            .setContentText("Macronium is keeping the connection alive")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .build()
    }

}