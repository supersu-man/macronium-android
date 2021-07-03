package com.supersuman.macronium

import org.junit.Test

import org.junit.Assert.*
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        thread {
            val s = Socket("192.168.29.221",6969)
            val pw = PrintWriter(s.getOutputStream())
            pw.write("yo")
            pw.flush()
            s.close()
        }
    }
}