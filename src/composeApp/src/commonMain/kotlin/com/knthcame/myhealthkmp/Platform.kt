package com.knthcame.myhealthkmp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform