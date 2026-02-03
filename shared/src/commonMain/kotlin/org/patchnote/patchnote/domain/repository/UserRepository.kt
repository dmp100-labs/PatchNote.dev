package org.patchnote.patchnote.domain.repository

import org.patchnote.patchnote.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUser(id: String): User
}