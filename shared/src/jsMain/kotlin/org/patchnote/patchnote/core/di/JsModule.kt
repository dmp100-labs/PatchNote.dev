package org.patchnote.patchnote.core.di

class JsModule {
}

// Todo : JS의존성 설정

/**
 * val jsModule = module {
 *     // JS 전용 네트워크 엔진 주입
 *     single { HttpClient(Js) {
 *         install(ContentNegotiation) { json() }
 *     } }
 *
 *     // JS 전용 로컬 저장소 등이 있다면 여기서 등록
 * }
 */