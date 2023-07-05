package com.lukasz.galinski.fluffy.presentation.common

import android.util.Log

fun Any.logDebug(message: String){
    Log.d(javaClass.simpleName, message)
}

fun Any.logInfo(message: String){
    Log.i(javaClass.simpleName, message)
}

fun Any.logError(message: String){
    Log.e(javaClass.simpleName, message)
}