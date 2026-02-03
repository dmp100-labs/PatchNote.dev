package org.patchnote.patchnote.data.mapper

import org.patchnote.patchnote.data.dto.UserDto
import org.patchnote.patchnote.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        id = this.id,
        name = this.name,
        email = this.email
    )
}