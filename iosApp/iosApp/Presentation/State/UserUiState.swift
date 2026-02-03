//
//  UserUiState.swift
//  iosApp
//
//  Created by 성규현 on 2/3/26.
//

import Shared

struct UserUiState {
    var users: [User] = []
    var isLoading: Bool = false
    var error: String? = nil
}
