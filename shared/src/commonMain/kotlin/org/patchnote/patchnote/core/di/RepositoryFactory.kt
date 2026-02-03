package org.patchnote.patchnote.core.di

import org.patchnote.patchnote.data.repositoryimpl.UserRepositoryImpl
import org.patchnote.patchnote.domain.repository.UserRepository

object RepositoryFactory {

    fun createUserRepository(): UserRepository {
        val remoteDataSource = DataSourceFactory.createUserRemoteDataSource()
        return UserRepositoryImpl(remoteDataSource)
    }
}