package com.example.plugins

import org.jetbrains.exposed.sql.Database
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

fun DI.MainBuilder.configureDI() {
    bind<Database>() with singleton { DatabaseFactory.create() }
    bind<UserService>() with singleton { UserService(instance()) }
}