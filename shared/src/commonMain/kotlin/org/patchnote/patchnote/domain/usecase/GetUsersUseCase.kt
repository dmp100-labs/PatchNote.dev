package org.patchnote.patchnote.domain.usecase


import org.patchnote.patchnote.domain.model.User
import org.patchnote.patchnote.domain.repository.UserRepository

class GetUsersUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(): List<User> {
        // Todo : 추후 Koin으로 의존성 자동주입.
        return repository.getUsers()
    }
}