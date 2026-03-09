package com.knthcame.myhealthkmp.ui.diary

import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class DiaryUIEvent(
    val localDate: LocalDate,
    val localTime: LocalTime,
    val value: String,
    val eventId: Int,
    val eventType: DiaryEvent.Type,
)
