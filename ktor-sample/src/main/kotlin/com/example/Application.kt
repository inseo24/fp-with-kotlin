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
import io.ktor.server.netty.*
import org.kodein.di.instance

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    kodeinApplication {
        bindSingleton { environment.config }
        bindSingleton { DatabaseFactory.create(instance()) }
        bindSingleton { UserService(instance()) }
        bindSingleton { Users.Controller(this.di) }
    }

    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
