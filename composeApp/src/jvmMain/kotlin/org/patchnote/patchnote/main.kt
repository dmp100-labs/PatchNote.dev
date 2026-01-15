package org.patchnote.patchnote

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    // Todo  : DI(Koin) 초기화 (브릿지없음)
    // 🚀 데스크톱 앱 시작 시 Koin 가동
    // initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "PatchNote11",
    ) {
        App()
    }
}

//fun initKoin() {
//    startKoin {
//        modules(sharedModule)
//        // modules(sharedModule, jvmModule)
//    }