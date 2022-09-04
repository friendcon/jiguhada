package com.project.jiguhada.util

import java.text.SimpleDateFormat
import java.util.Date

object CustomUtils {
    fun changeDateFormat(date: Date): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.format(date)
    }
}