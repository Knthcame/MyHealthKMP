package com.knthcame.myhealthkmp.data.diary.model

import kotlinx.datetime.Instant

data class DiaryEvent(
    val id: Int = 0,
    val value: String,
    val timeStamp: Instant,
    val type: Type,
) {
    enum class Type {
        Activity,
        Sleep,
    }
}
