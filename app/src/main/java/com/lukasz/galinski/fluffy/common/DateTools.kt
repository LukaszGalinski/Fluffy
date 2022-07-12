package com.lukasz.galinski.fluffy.common

import java.text.SimpleDateFormat
import java.util.*


private const val DATE_PATTERN = "dd-MM-yyyy"

fun getEndMonthDate(): Long {
    val cal = Calendar.getInstance()
    val month = cal.get(Calendar.MONTH)
    val year = cal.get(Calendar.YEAR)
    cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE))
    val lastDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
    val endDate = "$lastDayOfMonth-${month + 1}-$year"
    val date = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(endDate)
    return date?.time!!
}

fun getStartMonthDate(): Long {
    val cal = Calendar.getInstance()
    val month = cal.get(Calendar.MONTH)
    val year = cal.get(Calendar.YEAR)
    val startDate = "01-${month + 1}-$year"
    val date = SimpleDateFormat(DATE_PATTERN, Locale.getDefault()).parse(startDate)
    return date?.time!!
}

fun getCurrentDate(): Long {
    val calendar = Calendar.getInstance()
    val dateToday = calendar.time
    return dateToday.time
}

fun getCurrentMonth(): Int {
    val cal = Calendar.getInstance()
    return cal.get(Calendar.MONTH)
}