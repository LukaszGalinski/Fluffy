package com.lukasz.galinski.fluffy.common

import android.content.Context
import android.widget.Toast

fun Context.createToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
