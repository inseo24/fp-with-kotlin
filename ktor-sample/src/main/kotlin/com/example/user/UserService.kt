package com.example.user

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val name: String, val age: Int) {
    fun update(newName: String, newAge: Int): User {
        return this.copy(name = newName, age = newAge)
    }
}

class UserService(private val userRepository: UserRepository) {
    suspend fun create(user: User): Int = userRepository.create(user)
    suspend fun read(id: Int): User? = userRepository.read(id)
    suspend fun update(id: Int, user: User) = userRepository.update(id, user)
    suspend fun delete(id: Int) = userRepository.delete(id)
}
