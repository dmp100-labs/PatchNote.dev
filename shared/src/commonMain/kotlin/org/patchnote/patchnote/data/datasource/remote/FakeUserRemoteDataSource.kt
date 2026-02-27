package org.patchnote.patchnote.data.datasource.remote

import org.patchnote.patchnote.data.dto.UserDto

class FakeUserRemoteDataSource : UserRemoteDataSource {

    private val fakeUsers = listOf(
        UserDto("1", "Test", "Test@Test.com"),
    )

    override suspend fun getUsers(): List<UserDto> {
        return fakeUsers
    }

    override suspend fun getUser(id: String): UserDto {
        return fakeUsers.find { it.id == id }
            ?: throw Exception("User not found: $id")
    }
}