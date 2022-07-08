package com.lukasz.galinski.fluffy.view.account

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

fun highlightSelectedTextRange(
    message: CharSequence,
    startPosition: Int,
    endPosition: Int,
    color: String
): SpannableString {
    val spannable = SpannableString(message)
    spannable.setSpan(
        ForegroundColorSpan(Color.parseColor(color)),
        startPosition,
        endPosition,
        Spannable.SPAN_EXCLUSIVE_INCLUSIVE
    )
    return spannable
}