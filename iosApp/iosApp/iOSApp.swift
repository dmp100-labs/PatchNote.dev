import SwiftUI
// import Shared // 👈 shared 모듈을 import 해야 함

@main
struct iOSApp: App {
    // Todo : 앱 생성자 (가장 먼저 실행됨)
    //     init() {
    //         // 🚀 Kotlin에 만들어둔 initKoin() 함수 호출
    //         // (패키지명에 따라 KoinHelperKt.doInitKoin() 형태로 자동 변환될 수 있음)
    //         KoinHelperKt.doInitKoin()
    //     }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}