package org.patchnote.patchnote

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.patchnote.patchnote.presentation.screen.App

fun main() = application {
    // TODO: [Koin] 초기화
    Window(
        onCloseRequest = ::exitApplication,
        title = "PatchNote11",
    ) {
        App()
    }
}