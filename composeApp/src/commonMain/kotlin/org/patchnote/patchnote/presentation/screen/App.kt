package org.patchnote.patchnote.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.patchnote.patchnote.presentation.viewmodel.UserViewModel
import patchnote11.composeapp.generated.resources.Res
import patchnote11.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App() {
    // TODO: [Koin] koinViewModel()로 변경
    val viewModel: UserViewModel = viewModel { UserViewModel() }
    val uiState by viewModel.uiState.collectAsState()

    var showContent by remember { mutableStateOf(false) }

    MaterialTheme {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = {
                showContent = !showContent
                if (showContent) {
                    viewModel.loadUsers()
                }
            }) {
                Text("Load Users")
            }

            AnimatedVisibility(showContent) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Image(painterResource(Res.drawable.compose_multiplatform), null)

                    Spacer(modifier = Modifier.height(16.dp))

                    when {
                        uiState.isLoading -> {
                            CircularProgressIndicator()
                        }
                        uiState.error != null -> {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "Error: ${uiState.error}",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(onClick = { viewModel.retry() }) {
                                    Text("Retry")
                                }
                            }
                        }
                        uiState.users.isNotEmpty() -> {
                            Text(
                                text = "Users:",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            uiState.users.forEach { user ->
                                Text(user.displayText)
                            }
                        }
                        else -> {
                            Text("No users loaded")
                        }
                    }
                }
            }
        }
    }
}