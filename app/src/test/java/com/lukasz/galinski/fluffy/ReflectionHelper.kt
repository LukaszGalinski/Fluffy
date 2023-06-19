package com.lukasz.galinski.fluffy

import java.lang.reflect.Field

fun replacePrivateField(targetObject: Any, targetFieldName: String, toSet: Any): Field {
    val field = targetObject.javaClass.getDeclaredField(targetFieldName)
    field.isAccessible = true
    field.set(targetObject, toSet)
    return field
}

fun getPrivateFieldValue(targetObject: Any, targetFieldName: String, clazz: Class<*>): Any? {
    val field = targetObject.javaClass.getDeclaredField(targetFieldName)
    field.isAccessible = true

    return when (clazz){
        Long::class.java -> field.get(targetObject) as Long?
        String::class.java -> field.get(targetObject) as String?
        else -> { throw Exception("Unsupported class type") }
    }
}
