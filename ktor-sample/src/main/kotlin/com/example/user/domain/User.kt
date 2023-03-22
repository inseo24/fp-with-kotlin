package com.example.user.domain

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val age: Int) {
    fun update(newName: String, newAge: Int): User {
        return this.copy(name = newName, age = newAge)
    }
}