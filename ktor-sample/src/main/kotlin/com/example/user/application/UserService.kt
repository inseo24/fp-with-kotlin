package com.example.user.application

import com.example.user.infra.UserRepository
import com.example.user.domain.User

interface UserService {
    suspend fun create(user: User): Int
    suspend fun read(id: Int): User?
    suspend fun update(id: Int, user: User)
    suspend fun delete(id: Int)
}

class UserServiceImpl(private val userRepository: UserRepository) : UserService {
    override suspend fun create(user: User): Int = userRepository.create(user)
    override suspend fun read(id: Int): User? = userRepository.read(id)
    override suspend fun update(id: Int, user: User) = userRepository.update(id, user)
    override suspend fun delete(id: Int) = userRepository.delete(id)
}
