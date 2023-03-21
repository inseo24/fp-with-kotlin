package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.DI

/**
 * 애플리케이션 실행, 모듈 설정
 * */
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    val di = DI { configureDI() }

    configureHTTP()
    configureMonitoring()
    configureSerialization()
    configureRouting(di)
}
