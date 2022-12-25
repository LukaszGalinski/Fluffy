package com.lukasz.galinski.fluffy

import com.lukasz.galinski.fluffy.common.DateTools
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Test

private const val DUMMY_DATE_IN_LONG = 1660341600000 //13-08-2022
private const val DUMMY_DATE = "13-08-2022" //13-08-2022


class DateUnitTest {
    private var date = spyk<DateTools>()

    @Test
    fun getDateFromLong() {
        val dateFromLong = date.getDateFromLong(DUMMY_DATE_IN_LONG)
        assertEquals(DUMMY_DATE, dateFromLong)
    }

    @Test
    fun getLongFromDate() {
        val dateInLong = date.getLongValueFromDate(13, 7, 2022)
        assertEquals(DUMMY_DATE_IN_LONG, dateInLong)
    }
}