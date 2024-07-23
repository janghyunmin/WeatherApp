package com.kkjang.data.util

import java.util.Date

fun String?.default() = this ?: ""
fun Int?.default() = this ?: 0
fun Int?.idDefault() = this ?: -1
fun <T>List<T>?.default() = this ?: emptyList()
fun Boolean?.default() = this ?: false
fun Float?.default() = this ?: 0f
fun Double?.default() = this ?: 0.0
fun Long?.default() = this ?: 0L
fun Long?.isDefault() = this ?:-1
fun Date?.default() = this ?: Date()
fun Any?.default() = this ?: Any()