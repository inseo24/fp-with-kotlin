package com.example.user.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val age: Int) {
    fun update(request: User): User {
        return this.copy(name = request.name.takeIf { it.isNotEmpty() } ?: this.name, age = request.age.takeIf { it > 0 } ?: this.age)
    }
}