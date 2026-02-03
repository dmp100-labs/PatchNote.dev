package org.patchnote.patchnote.data.datasource.remote

import org.patchnote.patchnote.data.dto.UserDto

class FakeUserRemoteDataSource : UserRemoteDataSource {

    private val fakeUsers = listOf(
        UserDto("1", "홍길동", "hong@example.com"),
        UserDto("2", "김철수", "kim@example.com"),
        UserDto("3", "이영희", "lee@example.com")
    )

    override suspend fun getUsers(): List<UserDto> {
        return fakeUsers
    }

    override suspend fun getUser(id: String): UserDto {
        return fakeUsers.find { it.id == id }
            ?: throw Exception("User not found: $id")
    }
}