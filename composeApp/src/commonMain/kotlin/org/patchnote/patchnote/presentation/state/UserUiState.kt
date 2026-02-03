package org.patchnote.patchnote.presentation.state

import org.patchnote.patchnote.presentation.model.UserModel

data class UserUiState(
    val users: List<UserModel> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)