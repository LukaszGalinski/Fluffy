package com.lukasz.galinski.fluffy

import com.lukasz.galinski.fluffy.common.DateTools
import io.mockk.spyk
import org.junit.Assert.assertEquals
import org.junit.Test

class DateUnitTest {
    private val dateTools = spyk<DateTools>()
    private val dummyDateInLong = 1660341600000 //13-08-2022
    private val dummyDate = "13-08-2022"

    @Test
    fun getDateFromLong() {
        val dateFromLong = dateTools.getDateFromLong(dummyDateInLong)
        assertEquals(dummyDate, dateFromLong)
    }

    @Test
    fun getLongFromDate() {
        val dateInLong = dateTools.getLongValueFromDate(13, 7, 2022)
        assertEquals(dummyDateInLong, dateInLong)
    }
}