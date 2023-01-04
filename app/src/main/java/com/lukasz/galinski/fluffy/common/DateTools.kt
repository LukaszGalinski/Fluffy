package com.lukasz.galinski.fluffy.common

import java.text.SimpleDateFormat
import java.util.*


private const val DATE_PATTERN = "dd-MM-yyyy"

class DateTools {
    private val simpleDateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())

    fun getEndMonthDate(): Long {
        val cal = Calendar.getInstance()
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
        val lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        return getLongValueFromDate(lastDayOfMonth, month, year)
    }

    fun getStartMonthDate(): Long {
        val cal = Calendar.getInstance()
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        return getLongValueFromDate(1, month, year)
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

    fun getCurrentDateInLong(): Long {
        val calendar = Calendar.getInstance()
        val dateToday = calendar.time
        return dateToday.time
    }

    fun getCurrentMonthNumber(): Int {
        val cal = Calendar.getInstance()
        return cal.get(Calendar.MONTH)
    }
}