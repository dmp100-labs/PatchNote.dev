package org.patchnote.patchnote.presentation.model

import org.patchnote.patchnote.domain.model.User

data class UserModel(
    val id: String,
    val name: String,
    val email: String,
    val displayText: String
)

fun User.toUiModel() = UserModel(
    id = id,
    name = name,
    email = email,
    displayText = "$name ($email)"
)