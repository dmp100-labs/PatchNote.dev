package org.patchnote.patchnote.core.di

//// 🍎 iOS 전용 의존성 조립도
//val iosModule = module {
//    single {
//        // commonMain의 createHttpClient에 iOS 전용 Darwin 엔진을 꽂아줌
//        createHttpClient(Darwin.create())
//    }
//}