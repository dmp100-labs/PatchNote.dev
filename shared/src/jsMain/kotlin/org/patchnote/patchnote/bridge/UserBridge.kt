@file:OptIn(ExperimentalJsExport::class)

package org.patchnote.patchnote.bridge

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.promise
import org.patchnote.patchnote.bridge.mapper.toJsArray
import org.patchnote.patchnote.bridge.model.JsUser
import org.patchnote.patchnote.core.di.UseCaseFactory
import kotlin.js.Promise

@JsExport
class UserBridge {

    // 1. 의존성 가져오기 (나중에 Koin 도입 시 여기만 수정하면 됨)
    private val getUsersUseCase = UseCaseFactory.createGetUsersUseCase()

    fun getUsers(): Promise<Array<JsUser>> = GlobalScope.promise {
        val users = getUsersUseCase()
        users.toJsArray()
    }
}