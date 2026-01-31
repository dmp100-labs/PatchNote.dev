package org.patchnote.patchnote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.patchnote.patchnote.presentation.screen.App


// Todo : AndroidManifest.xml의 <application> 태그에 android:name=".MyApplication"을 꼭 추가

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Todo  : DI(Koin) 초기화 (브릿지없음)
//        // 🚀 안드로이드 앱 시작 시 Koin 가동
//        startKoin {
//            androidContext(this@MyApplication) // Context 주입
//            modules(sharedModule)
//            // modules(sharedModule, androidModule) // 안드로이드 전용 모듈 합체
//        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}