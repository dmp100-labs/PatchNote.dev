package org.patchnote.patchnote

class JsPlatform: Platform {
    override val name: String = "Web with Kotlin/JS"
}

actual fun getPlatform(): Platform = JsPlatform()

// Todo :  DB 드라이버 생성 코드만 jsMain(IndexedDB), androidMain(SQLite) 등에 actual로 작성하기