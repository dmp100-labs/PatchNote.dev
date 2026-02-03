package org.patchnote.patchnote.data.repositoryimpl

import org.patchnote.patchnote.data.datasource.remote.UserRemoteDataSource
import org.patchnote.patchnote.data.mapper.toDomain
import org.patchnote.patchnote.domain.model.User
import org.patchnote.patchnote.domain.repository.UserRepository

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getUsers(): List<User> {
        // TODO: 추후 Local DataSource 추가 시 캐싱 로직 구현
        return remoteDataSource.getUsers().map { it.toDomain() }
    }

    override suspend fun getUser(id: String): User {
        return remoteDataSource.getUser(id).toDomain()
    }
}