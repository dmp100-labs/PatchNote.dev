@file:OptIn(ExperimentalJsExport::class)

package org.patchnote.patchnote.bridge.model

import kotlin.js.JsExport

@JsExport
data class JsUser(
    val id: String,
    val name: String,
    val email: String
)