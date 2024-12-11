package com.knthcame.myhealthkmp.data.dashboard.repositories

import com.knthcame.myhealthkmp.data.datetime.repositories.DefaultDateTimeRepository
import com.knthcame.myhealthkmp.data.datetime.sources.DateTimeDao
import dev.mokkery.answering.returns
import dev.mokkery.every
import dev.mokkery.mock
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.hours

class DefaultDateTimeRepositoryTest {
    private lateinit var repository: DefaultDateTimeRepository

    private val dateTimeDao = mock<DateTimeDao>()

    @Test
    fun now_returnsValueFromDao() {
        val expected = Clock.System.now() - 3.hours
        every { dateTimeDao.now } returns expected
        initRepository()

        val actual = repository.now

        assertEquals(expected, actual)
    }

    @Test
    fun local_returnsValueFromDaoConvertedToLocalTimeZone() {
        val input = Clock.System.now() - 1.7.hours
        val timeZone = TimeZone.currentSystemDefault()
        every { dateTimeDao.now } returns input
        every { dateTimeDao.systemTimeZone } returns timeZone
        initRepository()

        val actual = repository.localNow

        assertEquals(input.toLocalDateTime(timeZone), actual)
    }

    @Test
    fun timeZone_returnsValueFromDao() {
        val expected = TimeZone.currentSystemDefault()
        every { dateTimeDao.systemTimeZone } returns expected
        initRepository()

        val actual = repository.systemTimeZone

        assertEquals(expected, actual)
    }

    private fun initRepository() {
        repository = DefaultDateTimeRepository(dateTimeDao)
    }
}