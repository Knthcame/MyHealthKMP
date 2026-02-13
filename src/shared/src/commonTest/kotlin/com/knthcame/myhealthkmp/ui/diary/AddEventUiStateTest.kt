package com.knthcame.myhealthkmp.ui.diary

import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent.Type.Activity
import com.knthcame.myhealthkmp.data.diary.model.DiaryEvent.Type.Sleep
import kotlinx.datetime.LocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddEventUiStateTest {
    private val initialState = AddEventUiState.initial(LocalDateTime(20206, 2, 13, 12, 0))

    @Test
    fun valueError_returnsNone_whenTypeIsActivityValueIsBlank() {
        val state = initialState.copy(value = "", entryType = Activity)

        assertEquals(expected = AddEventValidationError.None, actual = state.valueError)
        assertTrue(state.valueIsValid)
        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsNone_whenTypeIsSleepValueIsBlank() {
        val state = initialState.copy(value = "", entryType = Sleep)

        assertEquals(expected = AddEventValidationError.None, actual = state.valueError)
        assertTrue(state.valueIsValid)
        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsNone_whenTypeIsActivityAndValueIsInt() {
        val state = initialState.copy(value = "5", entryType = Activity)

        assertEquals(expected = AddEventValidationError.None, actual = state.valueError)
        assertTrue(state.valueIsValid)
        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsNone_whenTypeIsSleepAndValueIsFloat() {
        val state = initialState.copy(value = "4.2", entryType = Sleep)

        assertEquals(expected = AddEventValidationError.None, actual = state.valueError)
        assertTrue(state.valueIsValid)
        assertTrue(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsInvalidNumber_whenTypeIsActivityAndValueIsFloat() {
        val state = initialState.copy(value = "5.1", entryType = Activity)

        assertEquals(expected = AddEventValidationError.InvalidNumber, actual = state.valueError)
        assertFalse(state.valueIsValid)
        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsInvalidNumber_whenTypeIsActivityAndValueContainsLetters() {
        val state = initialState.copy(value = "5 min", entryType = Activity)

        assertEquals(expected = AddEventValidationError.InvalidNumber, actual = state.valueError)
        assertFalse(state.valueIsValid)
        assertFalse(state.isSaveEnabled)
    }

    @Test
    fun valueError_returnsInvalidNumber_whenTypeIsSleepAndValueContainsLetters() {
        val state = initialState.copy(value = "3 h", entryType = Sleep)

        assertEquals(expected = AddEventValidationError.InvalidNumber, actual = state.valueError)
        assertFalse(state.valueIsValid)
        assertFalse(state.isSaveEnabled)
    }
}