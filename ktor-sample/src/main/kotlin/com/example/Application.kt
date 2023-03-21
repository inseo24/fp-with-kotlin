package com.example

import com.example.plugins.DatabaseFactory
import com.example.plugins.UserService
import com.example.plugins.Users
import com.example.plugins.bindSingleton
import com.example.plugins.configureHTTP
import com.example.plugins.configureMonitoring
import com.example.plugins.configureSerialization
import com.example.plugins.kodeinApplication
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

/**
 * 애플리케이션 실행, 모듈 설정
 * */
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    kodeinApplication {
        bindSingleton { DatabaseFactory.create() }
        bind<UserService>() with singleton { UserService(instance()) }
        bindSingleton { Users.Controller(it) }
    }

    configureHTTP()
    configureMonitoring()
    configureSerialization()
}