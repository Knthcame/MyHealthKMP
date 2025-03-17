package com.knthcame.myhealthkmp.ui.diary

import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime

data class DiaryEventUiState(
    val entryDate: LocalDate,
    val entryTime: LocalTime,
    val entryType: DiaryEvent.Type,
    val value: String,
    val maxSelectableDate: LocalDate,
    val maxSelectableTime: LocalTime,
    val isSaved: Boolean,
) {
    companion object {
        fun initial(localDateTime: LocalDateTime) = DiaryEventUiState(
            entryDate = localDateTime.date,
            entryTime = localDateTime.time,
            entryType = DiaryEvent.Type.entries.first(),
            value = "",
            maxSelectableDate = localDateTime.date,
            maxSelectableTime = localDateTime.time,
            isSaved = false,
        )
    }

    val valueIsValid: Boolean
        get() = valueError == AddEventValidationError.None

    val valueError: AddEventValidationError = when {
        value.isBlank() -> AddEventValidationError.None
        else -> if (valueIsValid()) AddEventValidationError.None
        else AddEventValidationError.InvalidNumber
    }

    val isSaveEnabled: Boolean
        get() = value.isNotEmpty() && valueIsValid

    private fun valueIsValid() = when (entryType) {
        DiaryEvent.Type.Activity -> value.toIntOrNull() != null
        DiaryEvent.Type.Sleep -> value.toFloatOrNull() != null
    }
}

enum class AddEventValidationError {
    None, InvalidNumber,
}