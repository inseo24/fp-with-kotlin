package com.example.user.application

import com.example.user.infra.UserRepository
import com.example.user.domain.User

class UserService(private val userRepository: UserRepository) {
    suspend fun create(user: User): Int = userRepository.create(user)
    suspend fun read(id: Int): User? = userRepository.read(id)
    suspend fun update(id: Int, user: User) = userRepository.update(id, user)
    suspend fun delete(id: Int) = userRepository.delete(id)
}
