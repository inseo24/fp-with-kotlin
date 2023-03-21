package com.example.plugins

import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun create(config: ApplicationConfig): Database = Database.connect(
        url = config.property("ktor.database.url").getString(),
        user = config.property("ktor.database.user").getString(),
        driver = config.property("ktor.database.driver").getString(),
        password = config.property("ktor.database.password").getString()
    )
}
