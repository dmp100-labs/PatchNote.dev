//
//  UserViewModel.swift
//  iosApp
//
//  Created by 성규현 on 2/3/26.
//

import Foundation
import Shared

class UserViewModel: ObservableObject {
    @Published var uiState = UserUiState()

    private let getUsersUseCase = UseCaseFactory.shared.createGetUsersUseCase()

    @MainActor
    func loadUsers() async {
        self.uiState.isLoading = true
        self.uiState.error = nil

        do {
            let result = try await getUsersUseCase.invoke()
            self.uiState.users = result
            self.uiState.isLoading = false
        } catch {
            self.uiState.error = error.localizedDescription
            self.uiState.isLoading = false
        }
    }
}
