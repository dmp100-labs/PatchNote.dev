package org.patchnote.patchnote.data.datasource.remote


import org.patchnote.patchnote.data.dto.UserDto
    // TODO: RealUserRemoteDataSource: 서버 API 연동
interface UserRemoteDataSource {
    suspend fun getUsers(): List<UserDto>
    suspend fun getUser(id: String): UserDto
}