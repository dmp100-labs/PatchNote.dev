package org.patchnote.patchnote

import kotlin.js.ExperimentalJsExport
import kotlin.js.JsExport

// TODO : Create base package structure: data, (domain), each presentation

@OptIn(ExperimentalJsExport::class)
@JsExport
class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "테스테스트 You, ${platform.name}!"
    }
}