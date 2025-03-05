package com.knthcame.myhealthkmp.ui.diary

import androidx.lifecycle.ViewModel
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDateTime

class DiaryViewModel(
    private val diaryRepository: DiaryRepository,
    private val dateTimeRepository: DateTimeRepository,
    private val viewModelScope: CoroutineScope,
) : ViewModel(viewModelScope) {
    val groupedDiaryEvents: StateFlow<Map<LocalDate, List<DiaryUIEvent>>> =
        diaryRepository.diaryEvents.map { events ->
            groupEvents(events)
        }.stateIn(viewModelScope, SharingStarted.Lazily, emptyMap())

    fun delete(event: DiaryUIEvent) {
        viewModelScope.launch {
            diaryRepository.delete(event.eventType, event.eventId)
        }
    }

    private fun groupEvents(events: List<DiaryEvent>): Map<LocalDate, List<DiaryUIEvent>> {
        val uiEvents = events.map { diaryEvent ->
            val localDateTime =
                diaryEvent.timeStamp.toLocalDateTime(dateTimeRepository.systemTimeZone)

            DiaryUIEvent(
                localDate = localDateTime.date,
                localTime = localDateTime.time,
                value = diaryEvent.value,
                eventId = diaryEvent.id,
                eventType = diaryEvent.type,
            )
        }
        return uiEvents.groupBy { event -> event.localDate }
    }
}