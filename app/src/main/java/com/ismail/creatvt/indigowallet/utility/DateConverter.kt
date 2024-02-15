package com.ismail.creatvt.indigowallet.utility

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    companion object{
        @JvmStatic
        @TypeConverter
        fun fromTimestamp(timestamp: Long): Date {
            return Date(timestamp)
        }

        @JvmStatic
        @TypeConverter
        fun fromDate(date: Date): Long {
            return date.time
        }
    }

}