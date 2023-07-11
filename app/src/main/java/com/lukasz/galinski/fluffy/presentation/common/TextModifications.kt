package com.lukasz.galinski.fluffy.presentation.common

import android.content.res.Resources
import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.annotation.StringRes

fun highlightSelectedTextRange(message: CharSequence, startPosition: Int, endPosition: Int, color: String): SpannableString {
    val spannable = SpannableString(message)
    spannable.setSpan(ForegroundColorSpan(Color.parseColor(color)), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
    return spannable
}

fun Resources.getHtmlStringSpanned(@StringRes idRes: Int) = getString(idRes).toHtmlSpan()

fun Resources.getHtmlStringSpanned(@StringRes idRes: Int, vararg formatArgs: Any) =
    getString(idRes, *formatArgs).toHtmlSpan()

fun String.toHtmlSpan(): Spanned = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
