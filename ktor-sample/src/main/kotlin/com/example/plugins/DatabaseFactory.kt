package com.example.plugins

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun create(): Database = Database.connect(
        url = "jdbc:mysql://localhost:3307/user_service",
        user = "seoin",
        driver = "com.mysql.cj.jdbc.Driver",
        password = "tjdls@2278"
    )
}
