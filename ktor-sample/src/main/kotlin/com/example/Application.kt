package com.example

import com.example.plugins.DatabaseFactory
import com.example.plugins.bindSingleton
import com.example.plugins.configureHTTP
import com.example.plugins.configureMonitoring
import com.example.plugins.configureSerialization
import com.example.plugins.kodeinApplication
import com.example.user.application.UserServiceImpl
import com.example.user.controller.UserController
import com.example.user.infra.UserDatabaseRepository
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.kodein.di.instance

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    kodeinApplication {
        bindSingleton { environment.config }
        bindSingleton { DatabaseFactory.create(instance()) }
        bindSingleton { UserDatabaseRepository(instance()) }
        bindSingleton { UserServiceImpl(instance()) }
        bindSingleton { UserController(this.di) }
    }

    configureHTTP()
    configureMonitoring()
    configureSerialization()
}
