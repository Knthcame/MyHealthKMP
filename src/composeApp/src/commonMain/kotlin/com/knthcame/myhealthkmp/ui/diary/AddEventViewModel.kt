package com.knthcame.myhealthkmp.ui.diary

import androidx.lifecycle.ViewModel
import com.knthcame.myhealthkmp.data.common.timeTicker
import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration.Companion.minutes

private data class DiaryUserInputsState(
    val entryDate: LocalDate,
    val entryTime: LocalTime,
    val entryType: DiaryEvent.Type,
    val value: String,
) {
    companion object {
        fun default(localDateTime: LocalDateTime) = DiaryUserInputsState(
            entryDate = localDateTime.date,
            entryTime = localDateTime.time,
            entryType = DiaryEvent.Type.entries.first(),
            value = "",
        )
    }
}

class AddEventViewModel(
    private val logbookRepository: DiaryRepository,
    private val dateTimeRepository: DateTimeRepository,
    private val viewModelScope: CoroutineScope,
) : ViewModel(viewModelScope) {
    private val currentTimeZone = TimeZone.currentSystemDefault()

    private val userInputsState = MutableStateFlow(
        DiaryUserInputsState.default(dateTimeRepository.localNow)
    )
    private val savedState = MutableStateFlow(false)
    private val ticker = timeTicker(1.minutes)

    val uiState: StateFlow<DiaryEventUiState> = combine(
        userInputsState,
        savedState,
        ticker,
    ) { userInputs, isSaved, _ ->
        val currentLocalDateTime = dateTimeRepository.localNow
        DiaryEventUiState(
            entryDate = userInputs.entryDate,
            entryTime = userInputs.entryTime,
            entryType = userInputs.entryType,
            value = userInputs.value,
            maxSelectableDate = currentLocalDateTime.date,
            maxSelectableTime = currentLocalDateTime.time,
            isSaved = isSaved,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = DiaryEventUiState.initial(dateTimeRepository.localNow),
    )

    fun editEventDate(utcMillisecondsSinceEpoch: Long) {
        val instant = Instant.fromEpochMilliseconds(utcMillisecondsSinceEpoch)
        userInputsState.update { state ->
            state.copy(entryDate = instant.toLocalDateTime(currentTimeZone).date)
        }
    }

    fun editEventTime(hour: Int, minute: Int) {
        userInputsState.update { state ->
            state.copy(entryTime = LocalTime(hour, minute))
        }
    }

    fun editValue(value: String) {
        userInputsState.update { state ->
            state.copy(value = value)
        }
    }

    fun editEventType(type: DiaryEvent.Type) {
        userInputsState.update { state ->
            state.copy(entryType = type)
        }
    }

    fun saveEvent() {
        viewModelScope.launch {
            val timeStamp = LocalDateTime(
                uiState.value.entryDate, uiState.value.entryTime
            ).toInstant(currentTimeZone)

            logbookRepository.save(
                DiaryEvent(
                    value = uiState.value.value,
                    timeStamp = timeStamp,
                    type = uiState.value.entryType,
                )
            )
            savedState.update { true }
        }
    }
}