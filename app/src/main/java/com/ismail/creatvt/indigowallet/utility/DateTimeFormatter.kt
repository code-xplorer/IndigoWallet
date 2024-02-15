package com.ismail.creatvt.indigowallet.utility

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatter {

    private val simpleDateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
    private val simpleTimeFormatter = SimpleDateFormat("hh:mm a", Locale.ROOT)
    private val simpleDateTimeFormatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ROOT)

    @JvmStatic
    fun getFormattedDate(date: Date): String{
        return simpleDateFormatter.format(date)
    }

    @JvmStatic
    fun getFormattedTime(date: Date?): String{
        return simpleTimeFormatter.format(date?:Date())
    }

    @JvmStatic
    fun getDate(formatted: String): Date{
        return simpleDateFormatter.parse(formatted)?:Date()
    }

    @JvmStatic
    fun getTime(formatted: String): Date{
        return simpleTimeFormatter.parse(formatted)?:Date()
    }

    @JvmStatic
    fun getDate(date: String, time: String): Date{
        return simpleDateTimeFormatter.parse("$date $time")?:Date()
    }

}
