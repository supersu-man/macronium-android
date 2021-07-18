package com.supersuman.macronium

import org.junit.Test

import org.junit.Assert.*
import java.io.PrintWriter
import java.net.Socket
import kotlin.concurrent.thread

class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        var dlink = ""
        thread {
            var uri = "https://github.com/supersu-man/WhySave/releases/latest"
            val temp1 = khttp.get(uri)
            val temp2 = temp1.text.split("release-main-section")[1].split("Box Box--condensed")[1]
            val temp3 = temp2.split("<a href=\"")
            var temp4=""
            for(i in temp3){
                if (".apk" in i){
                    temp4 = i
                }
            }
            val temp5 = temp4.split(uri.replace("latest","").replace("https://github.com",""))[1].split("\"")[0]
            dlink = uri.replace("latest",temp5)
            println(dlink)
        }.join()
    }
}