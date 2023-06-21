package com.lukasz.galinski.core.domain

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private const val DATE_PATTERN = "dd-MM-yyyy"

class DateTimeOperations {
    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
    private val cal = Calendar.getInstance()

    /**
     * Take last date of the month in Long
     * Needs new instance to not distort actual date
     */
    fun getEndMonthDate(): Long {
        val calendar = Calendar.getInstance()
        with(calendar) {
            set(Calendar.DATE, getActualMaximum(Calendar.DATE))
            return getLongValueFromDate(
                get(Calendar.DAY_OF_MONTH),
                get(Calendar.MONTH),
                get(Calendar.YEAR)
            )
        }
    }

    fun getStartMonthDate(): Long {
        with(cal) {
            return getLongValueFromDate(
                1,
                get(Calendar.MONTH),
                get(Calendar.YEAR)
            )
        }
    }

    fun getLongValueFromDate(dayOfMonth: Int, month: Int, year: Int): Long {
        val endDate = "$dayOfMonth-${month + 1}-$year"
        val date = simpleDateFormat.parse(endDate)
        return date?.time!!
    }

    fun getDateFromLong(dateInLong: Long): String {
        val date = Date(dateInLong)
        return simpleDateFormat.format(date)
    }

    fun getCurrentDateInLong(): Long = cal.time.time

    fun getCurrentMonthNumber(): Int {
        return cal.get(Calendar.MONTH)
    }
}