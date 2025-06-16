package com.knthcame.myhealthkmp.ui.diary

import com.knthcame.myhealthkmp.data.datetime.repositories.DateTimeRepository
import com.knthcame.myhealthkmp.data.diary.repositories.DiaryRepository
import com.knthcame.myhealthkmp.utils.testViewModelScope
import dev.mokkery.answering.returns
import dev.mokkery.answering.returnsBy
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock

class AddEventViewModelTest {
    private lateinit var diaryRepository: DiaryRepository
    private lateinit var dateTimeRepository: DateTimeRepository
    private lateinit var viewModel: AddEventViewModel

    private val localDateTime: LocalDateTime
        get() = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

    @BeforeTest
    fun beforeTest() {
        diaryRepository = mock<DiaryRepository>()
        dateTimeRepository = mock<DateTimeRepository> {
            every { localNow } returnsBy { localDateTime }
        }
    }

    /**
     * [AddEventViewModel.uiState] returns the current date and time as the selected and
     * maximum datetime on init.
     **/
    @Test
    fun uiState_returnsCurrentDateTime_onInitialState() {
        val dateTime = localDateTime
        every { dateTimeRepository.localNow } returns dateTime
        initViewModel()

        assertEquals(dateTime.date, viewModel.uiState.value.entryDate)
        assertEquals(dateTime.time, viewModel.uiState.value.entryTime)
        assertEquals(dateTime.date, viewModel.uiState.value.maxSelectableDate)
        assertEquals(dateTime.time, viewModel.uiState.value.maxSelectableTime)
    }

    private fun initViewModel() {
        viewModel = AddEventViewModel(
            diaryRepository = diaryRepository,
            dateTimeRepository = dateTimeRepository,
            viewModelScope = testViewModelScope,
        )
    }
}