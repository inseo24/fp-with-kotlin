package com.example.user.infra

import com.example.user.domain.User

interface UserRepository {
    suspend fun create(user: User): Int
    suspend fun read(id: Int): User?
    suspend fun update(id: Int, user: User)
    suspend fun delete(id: Int)
}