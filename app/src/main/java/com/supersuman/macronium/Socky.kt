package com.supersuman.macronium

import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketException
import kotlin.concurrent.thread


class Socky(private val gridLayout: View) {

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
                showSnackBar("No connection made yet. Please connect.",gridLayout)
            }catch (e : Exception){
                showSnackBar(e.toString(), gridLayout)
            }

        }
    }

    fun connectSocket(address : String){
        thread {
            try {
                s.connect(InetSocketAddress(address, port))
                s.keepAlive = true
                showSnackBar("Connected successfully.",gridLayout)
            }catch (e : Exception){
                showSnackBar(e.toString(), gridLayout)
            }
        }.join()
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
                showSnackBar("Please re-connect.", gridLayout)
            }catch (e : UninitializedPropertyAccessException){
                showSnackBar("No connection made yet. Please connect.",gridLayout)
            }catch (e : Exception){
                showSnackBar(e.toString(), gridLayout)
            }
        }
    }

    fun showSnackBar( message: String, view: View){
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }
}