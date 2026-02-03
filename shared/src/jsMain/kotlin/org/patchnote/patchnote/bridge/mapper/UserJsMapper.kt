package org.patchnote.patchnote.bridge.mapper

import org.patchnote.patchnote.bridge.model.JsUser
import org.patchnote.patchnote.domain.model.User

fun User.toJsUser(): JsUser = JsUser(
    id = this.id,
    name = this.name,
    email = this.email
)

fun List<User>.toJsArray(): Array<JsUser> {
    return this.map { it.toJsUser() }.toTypedArray()
}