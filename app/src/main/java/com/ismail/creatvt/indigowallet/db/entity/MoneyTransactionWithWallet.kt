package com.ismail.creatvt.indigowallet.db.entity

import androidx.room.Embedded
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.ismail.creatvt.indigowallet.R
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_EXPENSE
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_INCOME
import com.ismail.creatvt.indigowallet.db.entity.MoneyTransaction.Companion.TYPE_TRANSFER
import java.util.*

data class MoneyTransactionWithWallet(
    @Embedded val transaction: MoneyTransaction,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "category_id"
    )
    val category: Category?,
    @Relation(
        parentColumn = "fromWallet",
        entityColumn = "wallet_id"
    )
    val fromWallet: Wallet?,
    @Relation(
        parentColumn = "toWallet",
        entityColumn = "wallet_id"
    )
    val toWallet: Wallet?
) {

    val iconRes: Int
        get() = if(transaction.type == TYPE_TRANSFER) R.drawable.ic_baseline_transfer_24 else category?.iconRes?:R.drawable.icon_category_question

    val colorRes: Int
        get() = if(transaction.type == TYPE_TRANSFER) R.color.md_grey_800 else category?.colorRes?:R.color.md_grey_700

    val walletString: String
        get() = when (transaction.type) {
            TYPE_INCOME -> toWallet?.name?:""
            TYPE_EXPENSE -> fromWallet?.name?:""
            else -> String.format("%s to %s", fromWallet?.name?:"", toWallet?.name?:"")
        }
}