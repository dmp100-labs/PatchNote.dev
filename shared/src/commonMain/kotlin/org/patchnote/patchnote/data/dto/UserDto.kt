package org.patchnote.patchnote.data.dto


// TODO: 추후 data/datasource/remote/model/request, response 로 서버에 맞춰서 이동 고려
// TODO: Serialization 추가 (@Serializable, kotlinx.serialization)
data class UserDto(
    val id: String,
    val name: String,
    val email: String
)