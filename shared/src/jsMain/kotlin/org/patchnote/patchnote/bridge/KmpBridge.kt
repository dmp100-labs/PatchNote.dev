package org.patchnote.patchnote.bridge

// todo : KmpBridge (초기화) -> import { KmpBridge } from 'shared';

//@JsExport
//object KmpBridge {
//    fun init() {
//        if (GlobalContext.getOrNull() == null) {
//            startKoin {
//                modules(sharedModule, jsModule)
//            }
//        }
//    }
//
//    // 리액트에서 new NoteBridge() 대신 사용 가능
//    fun createNoteBridge() = NoteBridge()
//}