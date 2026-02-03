package org.patchnote.patchnote.core.di

import org.patchnote.patchnote.domain.usecase.GetUsersUseCase

object UseCaseFactory {

    fun createGetUsersUseCase(): GetUsersUseCase {
        val repository = RepositoryFactory.createUserRepository()
        return GetUsersUseCase(repository)
    }
}