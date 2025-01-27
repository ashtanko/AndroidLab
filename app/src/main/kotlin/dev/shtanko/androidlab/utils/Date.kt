package dev.shtanko.androidlab.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.toDate(format: String = "yyyy-MM-dd'T'HH:mm:ss'Z'"): Date {
    return SimpleDateFormat(format, Locale.getDefault()).parse(this)
}
