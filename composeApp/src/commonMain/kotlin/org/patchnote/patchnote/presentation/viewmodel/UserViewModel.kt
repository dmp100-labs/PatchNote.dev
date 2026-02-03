package org.patchnote.patchnote.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.patchnote.patchnote.core.di.UseCaseFactory
import org.patchnote.patchnote.presentation.model.toUiModel
import org.patchnote.patchnote.presentation.state.UserUiState

class UserViewModel : ViewModel() {

    // TODO: [Koin] 생성자 주입으로 변경
    private val getUsersUseCase = UseCaseFactory.createGetUsersUseCase()

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val users = getUsersUseCase()
                _uiState.value = _uiState.value.copy(
                    users = users.map { it.toUiModel() },
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Unknown error"
                )
            }
        }
    }

    fun retry() {
        loadUsers()
    }
}