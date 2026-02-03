//
//  UserScreen.swift
//  iosApp
//
//  Created by 성규현 on 2/3/26.
//

import SwiftUI
import Shared

struct UserScreen: View {

    @StateObject private var viewModel = UserViewModel()
    @State private var showContent = false

    var body: some View {
        VStack {
            Button("Click me! (Load Users)") {
                withAnimation {
                    showContent.toggle()
                }
                if showContent {
                    Task { await viewModel.loadUsers() }
                }
            }
            .padding()
            if showContent {
                VStack(spacing: 16) {
                    Image(systemName: "swift")
                        .font(.system(size: 100))
                        .foregroundColor(.accentColor)
                    Group {
                        if viewModel.uiState.isLoading {
                            ProgressView("로딩 중...")
                        } else if let error = viewModel.uiState.error {
                            Text("에러: \(error)")
                                .foregroundColor(.red)
                        } else {
                            ScrollView {
                                VStack(alignment: .leading, spacing: 10) {
                                    ForEach(viewModel.uiState.users, id: \.id) { user in
                                        HStack {
                                            Text("👤")
                                            VStack(alignment: .leading) {
                                                Text(user.name).bold()
                                                Text(user.email).font(.caption).foregroundColor(.gray)
                                            }
                                        }
                                        .padding(.horizontal)
                                    }
                                }
                            }
                            .frame(maxHeight: 300)
                        }
                    }
                }
                .transition(.move(edge: .top).combined(with: .opacity))
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity, alignment: .top)
        .padding()
    }
}
