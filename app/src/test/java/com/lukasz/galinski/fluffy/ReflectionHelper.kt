package com.lukasz.galinski.fluffy

import java.lang.reflect.Field

fun replacePrivateField(targetObject: Any, targetFieldName: String, toSet: Any): Field {
    val field = targetObject.javaClass.getDeclaredField(targetFieldName)
    field.isAccessible = true
    field.set(targetObject, toSet)
    return field
}