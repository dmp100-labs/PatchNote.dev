package org.patchnote.patchnote.bridge

// Todo : NoteBridge (리액트용 UseCase/Repo 래퍼)

//@JsExport
//class UserBridge : KoinComponent { // Koin 기능 사용
//    private val repository: NoteRepository by inject()
//
//    // 리액트: await noteBridge.getNotes()
//    fun getNotes() = GlobalScope.promise {
//        val notes = repository.getNotes()
//        notes.toTypedArray() // List -> JS Array 변환
//    }
//}