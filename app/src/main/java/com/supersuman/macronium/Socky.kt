package com.supersuman.macronium

import android.content.Context
import android.os.Looper
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread


class Socky(val context: Context) {

    private var s = Socket()
    private val port = 6969

    fun disconnectSocket(){
        thread {
            try {
                if (s.isConnected){
                    s.close()
                    s= Socket()
                }
            }catch (e : UninitializedPropertyAccessException){
                myToast("No connection made yet. Please connect.")
            }catch (e : Exception){
                myToast(e.toString())
            }
        }
    }

    fun connectSocket(address : String){
        thread {
            try {
                s.connect(InetSocketAddress(address, port))
                s.keepAlive = true
                myToast("Connected successfully")
            }catch (e : Exception){
                myToast(e.toString())
            }
        }.join()
    }

    fun myToast(string: String){
        val handler = android.os.Handler(Looper.getMainLooper())
        handler.post {
            Toast.makeText(context, string,Toast.LENGTH_SHORT).show()
        }
    }

    fun isConnected(): Boolean {
        return try {
            s.isConnected
        }catch (e:Exception){
            false
        }
    }

    fun sendMessage(message : String) {
        thread {
            try {
                val pw = PrintWriter(s.getOutputStream())
                pw.write(message)
                pw.flush()
            }catch (e : SocketException) {
                myToast(e.toString())
            }catch (e : UninitializedPropertyAccessException){
                myToast(e.toString())
            }catch (e : Exception){
                myToast(e.toString())
            }
        }
    }

    fun showSnackBar( message: String, view: View){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}