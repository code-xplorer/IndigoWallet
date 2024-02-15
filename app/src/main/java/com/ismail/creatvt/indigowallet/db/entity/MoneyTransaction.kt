package com.ismail.creatvt.indigowallet.db.entity

import androidx.annotation.IntDef
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.utility.ColorFactory
import java.text.SimpleDateFormat
import java.util.*

@Entity
data class MoneyTransaction(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var name: String,
    var timestamp: Date,
    var amount: Float,
    var categoryId: Int,
    var type: Int,
    var fromWallet: Int,
    var toWallet: Int,
    var hasList: Boolean = false,
    var description: String? = null
) {
    companion object {
        const val TYPE_INCOME = 0
        const val TYPE_EXPENSE = 1
        const val TYPE_TRANSFER = 2
    }

    val hasDescription: Boolean
        get() = !description.isNullOrEmpty()

    val dateString: String
        get() {
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.ROOT)
            return formatter.format(timestamp)
        }

    val timeString: String
        get() {
            val formatter = SimpleDateFormat("hh:mm a", Locale.ROOT)
            return formatter.format(timestamp)
        }

    @IntDef(
        TYPE_INCOME,
        TYPE_EXPENSE,
        TYPE_TRANSFER
    )
    annotation class TransactionType

    val amountValue: String
        get() = when (type) {
            TYPE_INCOME -> String.format("+₹%.2f", amount)
            TYPE_EXPENSE -> String.format("-₹%.2f", amount)
            TYPE_TRANSFER -> String.format("₹%.2f", amount)
            else -> String.format("$%.2f", amount)
        }

    val typeColor: Int
        get() = when (type) {
            TYPE_INCOME -> R.color.green_500
            TYPE_EXPENSE -> R.color.red_500
            TYPE_TRANSFER -> R.color.grey_800
            else -> R.color.grey_500
        }

}