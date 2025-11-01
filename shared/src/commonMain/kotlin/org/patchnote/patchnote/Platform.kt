package org.patchnote.patchnote

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform