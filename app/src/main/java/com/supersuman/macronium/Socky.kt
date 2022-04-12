package com.supersuman.macronium

import android.content.Context
import android.os.Looper
import android.widget.Toast
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.client.Socket.EVENT_CONNECT
import io.socket.client.Socket.EVENT_DISCONNECT
import io.socket.emitter.Emitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.URI
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext


class Socky(private val context: Context) {

    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private val port = 6969
    private var socket: Socket? = null

    fun connectSocket(address : String) = coroutineScope.launch {
        try {
            socket = IO.socket(URI.create("http://$address:$port"))
            socket?.on(EVENT_CONNECT) {
                myToast("Connected to server")
            }
            socket?.connect()
        } catch (e: Exception) {
            myToast(e.toString())
        }
    }

    fun disconnectSocket() = coroutineScope.launch {
        socket?.disconnect()
    }

    private fun myToast(string: String) = coroutineScope.launch(Dispatchers.Main){
        Toast.makeText(context, string,Toast.LENGTH_SHORT).show()
    }

    fun isConnected(): Boolean? {
        return socket?.connected()
    }

    fun sendMessage(event : String, arg : String) = coroutineScope.launch {
        socket?.emit(event, arg)
    }
}